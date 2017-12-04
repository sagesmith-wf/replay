(ns replay.core
  (:require [clojure.contrib.command-line :as ccl]
            [replay.puller :as puller]
            [replay.pusher :as pusher]
            [replay.processor :as processor])
  (:gen-class))

(defn run-replay
  [call-map]
  (let
    [{begin-tx :begin-tx end-tx :end-tx b-uri :begin-uri e-uri :end-uri} call-map
     begin-uri (if (= ::undefined b-uri) (str "eva:mem://" (gensym)) b-uri)
     end-uri (if (= ::undefined e-uri) (str "eva:mem://" (gensym)) e-uri)]
  (println "Beginning Replay")
  (doseq [x (range begin-tx (inc (puller/last-transaction {:uri begin-uri})))]
    (pusher/push {:uri end-uri
                  :txs (processor/process {:txs (puller/pull-transactions {:uri begin-uri
                                                                           :begin-tx x
                                                                           :end-tx (inc x)})})}))
  (println "Finished Replay")))

(defn -main
  "Does replay."
  [& args]
  (ccl/with-command-line args
       "Breaks out accepted functions"
     [[begin-tx "begin transaction" 0]
      [end-tx "end transaction"]
      [begin-uri "the uri of the connection to pull from" ::undefined]
      [end-uri "the uri of the connection to pull from" ::undefined]
      extras]
     (run-replay {:begin-tx begin-tx :end-tx end-tx :begin-uri begin-uri :end-uri end-uri}))
  (System/exit 0))

