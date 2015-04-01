(defproject defbot "0.1.0-SNAPSHOT"
  :description "A slack bot for clojure"
  :url "http://github.com/ragnard/defbot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.3.1"]
                 [aleph "0.4.0-beta3"]
                 [clojail "1.0.6"]
                 [org.apache.commons/commons-lang3 "3.3.2"]
                 
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.slf4j/log4j-over-slf4j "1.7.12"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :jvm-opt ["-Djava.security.policy=security.policy"]
  :profiles {:dev {:source-paths ["dev"]}
             :nix {:local-repo "/tmp/m2"}
             :uberjar {:aot :all}}
  :main defbot.cli
  :uberjar-name "defbot.jar")
