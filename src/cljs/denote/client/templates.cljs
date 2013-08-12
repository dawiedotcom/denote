(ns denote.client.templates
  (:require [denote.client.html :as html]))

(def edit-label (html/tag "span" {:class "glyphicon glyphicon-edit"}))

(defn button 
  "A generic bootstrap styled button"
  [label onclick]
  (html/tag "button" 
    {:onclick onclick
     :class "btn btn-default btn-mini"} 
    label))

(defn done-edit-button 
  "Displayed while editing markup - renders content on click"
  [id]
  (button "ok" (str "denote.client.markup.renderClicked('" id "')")))

(defn edit-button 
  "Displayed while reading html - goes to edit mode on click"
  [id]
  (button edit-label (str "denote.client.markup.edit('" id "')")))

(defn edit-area 
  "The text area when editing markup"
  [markup-content]
  (html/div {:class "form-group"}
  (html/div {:class "coll-11"}
    (html/textarea {:class "form-control" :id "mdarea"} markup-content))))

(defn par 
  "For displaying rendered html"
  [content id] 
  (str (html/div {:class "col-11"} content)
       (html/div {:class "col-1"} (edit-button id))))

(defn div-par [content id]
  (html/div 
    {:class "row" :id id} 
    (par content id)))
