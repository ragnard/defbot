(ns defbot.bot
  (:require [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [aleph.http :as http]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [byte-streams :as bs]
            [clojail.core :as clojail]
            [clojail.testers])
  (:import [java.util.concurrent.atomic AtomicInteger]
           [org.apache.commons.lang3 StringEscapeUtils]))

(defn unescape-html-entities
  [s]
  (StringEscapeUtils/unescapeHtml4 s))

;;--------------------------------------------------------------------
;; slack integration

(def slack-api "https://slack.com/api/")

(defn slack-rtm-start-url
  [token]
  (str slack-api "rtm.start?token=" token))

(defn read-body
  [res]
  (-> res :body bs/to-reader (json/read :key-fn keyword)))

(defn throw-on-exception
  [res]
  (if-not (:ok res)
    (throw (ex-info (format "Request was unsuccessful: %s" (:error res))
                    {:response res}))
    res))

(defn slack-rtm-start
  [token]
  (d/chain (http/get (slack-rtm-start-url token))
           read-body
           throw-on-exception))

(defn slack-rtm-loop
  [stream event-handler init]
  (let [send! (let [counter (AtomicInteger. 0)]
                (fn [req]
                  (let [message (assoc req :id (.getAndIncrement counter))]
                    (s/put! stream (json/write-str message)))))]
    (loop [state init]
      (if (s/closed? stream)
        (throw (ex-info "Stream was closed" {})))
      (let [val @(s/try-take! stream ::failed 5000 ::timeout)]
        (cond
          (= ::timeout val)
          (do
            (log/debugf "sending ping")
            (send! {:type "ping"})
            (recur state))
          
          (= ::failed val)
          (do
            (log/warnf "Unable to read from stream")
            (recur state))

          :else
          (let [event (json/read-str val :key-fn keyword)
                _ (log/debugf "got event '%s'" (pr-str event))
                res (event-handler event state send!)]
            (recur (or res state))))))))

;;--------------------------------------------------------------------
;; bot

(def sandbox (clojail/sandbox clojail.testers/secure-tester))

(defmulti handle-event
  (fn [{:keys [type] :as event} state send!]
    (keyword type)))

(defmethod handle-event :default
  [event state send!])

(defmethod handle-event :message
  [{:keys [channel text]} state send!]
  (when (.startsWith text ",")
    (try
      (let [form (clojail/safe-read (unescape-html-entities text))
            _ (log/debugf "read form for eval: '%s'" (pr-str form))
            res (sandbox form)
            _ (log/debugf "eval result: '%s'" (pr-str res))]
        (send! {:type "message"
                :channel channel
                :text (format "```%s```" (pr-str res))})
        state)
      (catch Exception e
        (send! {:type "message"
                :channel channel
                :text (format "```%s: %s```" (.. e getClass getName) (.getMessage e))})
        state))))

(defn run-bot!
  [token]
  (let [res @(slack-rtm-start token)]
    (slack-rtm-loop @(http/websocket-client (:url res))
                    handle-event
                    res)))


(comment

  (run-bot! (slurp "slack.token"))

  )


