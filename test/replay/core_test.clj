(ns replay.core-test
  (:require [clojure.test :refer :all]
            [replay.core :refer :all :as replay]
            [eva.api :refer :all :as eva]
            [eva.api.java.util :refer :all]
            [replay.puller :refer :all :as puller]
            [clojure.java.io :as io]
            [replay.eva-utils :refer :all :as utils]))

(def xal-create-example
  "[[create
     [:Artifact
        {:MediaType \"application/json\",
         :FsResourceId \"ZnMtdjE6UVdOamIzVnVkQjgxT1RRNU1ERTJPREl4T1RnNU16YzIveGJybC82ZWZjZmM4MGVmMzQ0ODQ2YWZmZTZlNWVlZGVkZmNlNw\",
         :IsRequired false,
         :parent #uuid \"787fb71c-223a-43ce-82ee-389aa0dc148b\",
         :Label \"DocumentMetadata.json\",
          :id \"1\",
          :Audience 0,
          :GenerationStatus 2}]
     [:Artifact
        {:MediaType \"application/xhtml+xml\",
         :FsResourceId \"ZnMtdjE6UVdOamIzVnVkQjgxT1RRNU1ERTJPREl4T1RnNU16YzIveGJybC80M2U0M2QxNjFhZjI0Yjc2YmRhYzc0YWVkMjE1OTZlYQ\",
         :IsRequired false,
         :parent #uuid \"787fb71c-223a-43ce-82ee-389aa0dc148b\",
         :Label \"1373145340464216.htm\",
         :id \"2\",
         :Audience 1,
         :GenerationStatus 2,
         :DocumentId \"1373145340464216\"}]]
     {:xal \"0.0.1\" :api \"2.0.7\" :db nil}
     {}]")

(def xal-db-add
  [{:db/id (eva/tempid :db.part/db)
   :xal xal-create-example}])

(def schema
  (nth (eva.api.java.util/read-all (io/resource "schema.edn")) 0))

(deftest run-replay:test
  (testing "Does this crud even works"
    (let [begin-uri (str "eva:mem://" (gensym))
          end-uri (str "eva:mem://" (gensym))]
      @(eva/transact (utils/conn {:uri begin-uri}) schema)
      @(eva/transact (utils/conn {:uri begin-uri}) xal-db-add)
      @(eva/transact (utils/conn {:uri end-uri}) schema)
    (replay/run-replay {:begin-uri begin-uri :begin-tx 2 :end-uri end-uri})
      (println "VALUE OF REPLAY" (:v (first (:data (first (puller/pull-transactions {:uri end-uri :begin-tx 2})))))))))
