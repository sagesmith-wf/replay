(ns replay.puller
  (:require [eva.api :refer :all :as eva]
            [replay.eva-utils :refer :all :as utils]))

(defn last-transaction
  "Gets the last transaction that occurred in the database"
  [call-map]
  (let
    [{uri :uri end-tx :end-tx} call-map]
    (if (nil? end-tx)
      (basis-t (db (utils/conn {:uri uri})))
      nil)))

(defn pull-transactions
  "Pulls the transactions between the passed tx range from the passed uri"
  [call-map]
  (let
    [{uri :uri begin-tx :begin-tx end-tx :end-tx} call-map]
  (tx-range (log (utils/conn {:uri uri})) begin-tx end-tx)))
