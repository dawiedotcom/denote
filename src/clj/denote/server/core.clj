(ns denote.server.core
  (:gen-class :main true)
  (:use ring.adapter.jetty)
  (:require [denote.server.service :as service]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  ;(alter-var-root #'*read-eval* (constantly false))
  ;(println "Hello, World!"))
  (run-jetty #'service/app {:port 8000}))
