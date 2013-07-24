(ns denote.server.pandoc
  (use
   [clojure.java.shell :only [sh]]))

(defn pandoc [in-format str]
  (sh "pandoc" "-t" "html" "-f" (name in-format) :in str))
