(ns defbot.cli
  (:require [clojure.tools.logging :as log]
            [defbot.bot :as bot])
  (:gen-class))

(defn print-usage
  []
  (println (str "usage: defbot [TOKEN]\n\n"
                "Start a defbot using slack API token TOKEN.")))

(defn -main
  [& args]
  (when-not (= 1 (count args))
    (print-usage)
    (System/exit 1))

  (let [[token] args]
    (log/infof "Running bot with token '%s'" token)
    (bot/run-bot! token)))


