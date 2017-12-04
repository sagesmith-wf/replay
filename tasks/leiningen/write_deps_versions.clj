(ns leiningen.write-deps-versions)

(defn write-deps-versions
      [project & deps]
      (assert (get project :write-deps-versions) ":write-deps-versions not specified in project.clj")
      (let [output-file (get project :write-deps-versions)
            proj-deps (get project :dependencies)
            dep-vers (into {} (for [[dep ver] proj-deps]
                                   [dep ver]))]
           (spit output-file (pr-str dep-vers))))
