(ns denote.server.io
  (:use
    [clojure.java.io :only [file as-file]]
    [clojure.string :only [split join]]))

(declare ls-markup
         dir|file
         extention) 

(def markup-extentions #{"md" "markdown" "org"})

(defn get-content [filename]
  (let [f (as-file filename)]
    (if (.exists f)
      (slurp filename))))

(defn choose-file [uri]
  (let [[directory filename-stem] (dir|file uri)
        filename (first (ls-markup directory filename-stem))]
    {:content (slurp filename)
     :ext (extention filename)}))

;;; Helpers

(defn extention [filename]
  (peek (split filename #"\.")))

(defn markup? [f]
  (contains? markup-extentions 
             (extention f)))

(defn ls-markup [dir-name filename-stem]
  (let [dir (file dir-name)
        files (file-seq dir)]
    (filter #(and (markup? %) 
                  (re-find (re-pattern filename-stem) %))
            (map #(.getName %) files))))

(defn dir|file [uri]
  (let [parts (split uri #"/")
        file (peek parts)
        dir- (join "/" (pop parts))
        dir (if (empty? dir-) "." dir-)]
    [dir file]))
