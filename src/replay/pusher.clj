(ns replay.pusher
  (:require [eva.api :refer :all :as eva]
            [replay.eva-utils :refer :all :as utils]))

(defn build-tx
  [call-map]
  (let [{tx :tx} call-map]
    [{:db/id (eva/tempid :db.part/db)
      :xal (str tx)}]))

(defn push
  "Transacts the specified transactions in order to the specified uri"
  [call-map]
  (let [{uri :uri txs :txs} call-map]
    (doseq [tx txs] @(eva/transact (utils/conn {:uri uri}) (build-tx {:tx tx})))))
