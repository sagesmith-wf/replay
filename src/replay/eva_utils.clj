(ns replay.eva-utils
  (:require [eva.api :refer :all :as eva]))


(defn conn
  "Creates a connection to the specified uri"
  [call-map]
  (let [{uri :uri} call-map]
    (eva/connect uri)))
