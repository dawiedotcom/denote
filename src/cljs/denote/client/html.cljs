(ns denote.client.html
  (:require [clojure.string :as string]))

(defn attrs [atter-map]
  (string/join " " (for [[k v] atter-map] 
                     (str (name k) "=\"" v "\""))))
  
(defn tag [tag-name attr-map & inner-text]
  (str "<" tag-name " " (attrs attr-map) ">" 
       (apply str inner-text) 
       "</" tag-name ">"))

(defn div [attr-map]
  (let [div (.createElement js/document "div")]
    (doseq [[k v] attr-map]
      (aset div (name k) v))
    div))


