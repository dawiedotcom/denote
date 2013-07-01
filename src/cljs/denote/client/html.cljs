(ns denote.client.html
  (:require [clojure.string :as string]))

(defn attrs [atter-map]
  (string/join " " (for [[k v] atter-map] 
                     (str (name k) "=\"" v "\""))))
  
(defn tag [tag-name attr-map inner-text]
  (str "<" tag-name " " (attrs attr-map) ">" inner-text "</" tag-name ">"))
