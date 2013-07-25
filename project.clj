(defproject denote "0.1.0-SNAPSHOT"
  :description "A browser based markup editor."
  :url "http://github.com/dawiedotcom/denote"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring "1.0.2"]
                 [fogus/ring-edn "0.2.0-SNAPSHOT"]
                 [domina "1.0.0"]
                 [com.google.javascript/closure-compiler "r1592"]
                 [org.clojure/google-closure-library "0.0-790"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]]
  :hooks [leiningen.cljsbuild]
  :extra-classpath-dirs ["checkouts/clojurescript/src/clj"
                         "checkouts/clojurescript/src/cljs"]
  :dev-dependencies [[lein-cljsbuild "0.3.2"]]
  :plugins [[lein-ring "0.8.5"]
            [lein-cljsbuild "0.3.2"]]
  :ring {:handler denote.server.service/app}
  :source-paths ["src/clj"]
  :main denote.server.core
  :cljsbuild 
  {:builds[{:source-paths ["src/cljs"]
            :jar true
            :compiler 
            {:output-dir "resources/public/javascript/gen/"
             :output-to "resources/public/javascript/denote.js"
             :pretty-print true
             :optimizations :simple}}]}
  :profiles
  {:dev {:dependancies [[ring-mock "0.1.5"]]}})
