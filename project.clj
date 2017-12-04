(defproject replay "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure  "1.9.0-alpha17"]
                 [com.workiva/eva "0.4.9"]
                 [com.workiva.xbrl/mdk-core "0.11.0"]
                 [com.workiva.xbrl/mdk-test "0.11.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [com.workiva.xbrl/xbrl2-server-api "1.0.3"]]
  :write-deps-versions "resources/deps-versions-output.edn"
  :repositories [["workiva-release" {:url "https://artifactory.workiva.org/artifactory/workiva-release"}]]
  :main ^:skip-aot replay.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
