(def +version+ "0.0.5")

(set-env!
  :resource-paths #{"src"}
  :dependencies '[
                  [org.clojure/clojure "1.8.0" :scope "provided"]
                  [mkarp/degree9-boot-npm "1.10.2"]
                  [me.raynes/conch "0.8.0"]
                  ]
  :repositories #(conj %
                       ["clojars" {:url "https://clojars.org/repo/"
                                   :username (System/getenv "CLOJARS_USER")
                                   :password (System/getenv "CLOJARS_PASS")}]
                       ["artifactory" {:url (or
                                              (System/getenv "ARTIFACTORY_URL")
                                              (str "http://104.198.42.43:8081/artifactory/libs-"
                                                   (if (.contains +version+ "SNAPSHOT") "snapshot" "release")
                                                   "-local"))
                                       :username (System/getenv "ARTIFACTORY_USER")
                                       :password (System/getenv "ARTIFACTORY_PASS")}]))

(require '[boot.task.built-in :refer [push]])

(deftask build
  "Build the jar in prep for deploy."
  []
  (comp
    (pom)
    (jar)
    identity))

(deftask dev
  "Build a jar and install to local maven repo."
  []
  (comp
    (watch)
    (build)
    (install)
    identity))

(deftask deploy
  []
  (comp
    (build)
    (push :repo "artifactory")))

(task-options!
  pom {:project 'mkarp/lwhorton-boot-stylus
       :version +version+
       :description "Boot task to compile stylus files to css-module clojure namespaces."
       :url "https://github.com/mkarp/lwhorton-boot-stylus"
       :scm {:url "https://github.com/mkarp/lwhorton-boot-stylus"}
       }
  )
