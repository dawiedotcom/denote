(ns denote.client.html
  (:require [clojure.string :as string]))


;; HTML 

(defn attrs [atter-map]
  (string/join " " (for [[k v] atter-map] 
                     (str (name k) "=\"" v "\""))))
  
(defn tag [tag-name attr-map & inner-text]
  (str "<" tag-name " " (attrs attr-map) ">" 
       (apply str inner-text) 
       "</" tag-name ">"))

(defn div [attr-map & inner-text]
  (apply tag "div" attr-map inner-text))

(defn textarea [attr-map & inner-text]
  (apply tag "textarea" attr-map inner-text))


