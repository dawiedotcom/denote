(ns denote.client.markup
  (:require [domina :as dom]
            [cljs.reader :as reader]
            [denote.client.html :as html])
  (:use [domina.css :only [sel]]))

(defn map->js [m]
  (let [out (js-obj)]
    (doseq [[k v] m]
      (aset out (name k) v))
    out))

(def *markup* (atom {}))

;; Templating

(defn button [label onclick]
  (html/tag "button" {:onclick onclick} label))

(defn done-edit-button [id]
  (let [res (button "ok" (str "denote.client.markup.renderClicked('" id "')"))]
    (console.log res)
    res))


(defn edit-button [id]
  (button "edit" (str "denote.client.markup.edit('" id "')")))


;; Client/Server coms

(defn callback [body]
  (let [response (reader/read-string body)
        id (:content-id response)
        content (:html response)
        div (dom/by-id id)]
    (reset! *markup* (assoc @*markup* id (:markup response)))
    (dom/destroy-children! div)
    (dom/set-inner-html! div (str content (edit-button id)))))

(defn postmd [md content-id]
  (let [data {:format :markdown 
              :content  (str md)
              :content-id content-id}]
    (.ajax js/jQuery (map->js {:url "/"
                               :type "POST"
                               :contentType "application/edn"
                               :data (str data)
                               :success callback}))))

;; Public

(defn ^:export edit [id]
  (let [div (dom/by-id id)]
    (dom/destroy-children! div)
    (dom/append! div (str "<textarea id=\"mdarea\">" (get @*markup* id) "</textarea>"))
    (dom/append! div (done-edit-button id))))
    
(defn ^:export renderClicked [content-id]
  (let [mdarea (.getElementById js/document "mdarea")]
    (postmd (.-value mdarea) content-id)))
