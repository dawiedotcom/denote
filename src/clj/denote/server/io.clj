(ns denote.server.io
  (:gen-class)
  (:use
    [clojure.java.io :only [file as-file]]
    [clojure.string :only [split join]]))

(declare ls-markup
         dir|file) 

(def markup-extentions #{"md" "markdown" "org"})

(defn get-content [filename]
  (let [f (as-file filename)]
    (if (.exists f)
      (slurp filename))))

(defn choose-file [uri]
  (println uri)
  (let [[directory filename-stem] (dir|file uri)
        files (ls-markup directory filename-stem)]
    (println directory filename-stem files)
    (slurp (first files))))

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
