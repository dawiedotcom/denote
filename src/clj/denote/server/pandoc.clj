(ns denote.server.pandoc
  (:gen-class)
  (use
   [clojure.java.shell :only [sh]]))

(defn pandoc [in-format str]
  (sh "pandoc" "-t" "html" "-f" (name in-format) :in str))
