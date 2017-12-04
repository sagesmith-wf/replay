(ns replay.processor)

(def api-versions
  ["2.0.7"])

(def xal-versions
  ["0.0.1"])

(defmulti convert-api :api)

(defmethod convert-api (first api-versions) [call-map]
  (update-in (:xal-object call-map) [1 :api] nil?))

(defmethod convert-api :default [call-map]
  (println "UNSUPPORTED VERSION OF API:" (:api call-map)))

(defmulti convert-xal :xal)

(defmethod convert-xal "0.0.1" [call-map]
  (update-in (:xal-object call-map) [1 :xal] nil?))

(defmethod convert-xal :default [call-map]
  (println "UNSUPPORTED VERSION OF XAL:" (:xal call-map)))

(defn do-conversions
  [call-map]
  (let [{xal-version :xal-version api-version :api-version uuid-map :uuid-map xal-object :xal-object} call-map
        xal-object (first (for [api (range (.indexOf api-versions api-version) (count api-versions))]
                     (convert-api {:api (nth api-versions api) :xal-object xal-object})))
        xal-object (for [xal (range (.indexOf xal-versions xal-version) (count xal-versions))]
                     (convert-xal {:xal (nth xal-versions xal) :xal-object xal-object}))]
    xal-object))

(defn actions-contains-uuid-map
  "Handles the case where a there is a uuid-map on a xal-object"
  [call-map]
  (let [{xal-object :xal-object} call-map
        xal-version (:xal (second xal-object))
        api-version (:api (second xal-object))
        uuid-map (last xal-object)]
      (do-conversions
        {:api-version api-version
         :xal-version xal-version
         :uuid-map uuid-map
         :xal-object xal-object})))

(defn actions-does-not-contains-uuid-map
  "Handles the case where there isn't a uuid-map on a xal-object"
  [call-map]
  (let [{xal-object :xal-object} call-map
        xal-version (:xal (last xal-object))
        api-version (:api (last xal-object))]
    (do-conversions
      {:xal-version xal-version
       :api-version api-version})))

(defn process
  "
  Processes all xal objects
  "
  [call-map]
  (let [{txs :txs} call-map
        _ (println "TXS DUDE" (:v (first (:data (first txs)))))
        xal-object (clojure.edn/read-string (:v (first (:data (first txs)))))]
    (if  (contains? (last xal-object) :xal)
      (actions-does-not-contains-uuid-map {:xal-object xal-object})
      (actions-contains-uuid-map {:xal-object xal-object}))))
