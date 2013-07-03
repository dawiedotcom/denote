(ns denote.server.service
  (:gen-class)
  (:use compojure.core
        [denote.server.pandoc :only [pandoc]]
        [denote.server.io :only [choose-file]]
        [ring.middleware.edn :only [wrap-edn-params]]
        [ring.middleware.content-type :only [wrap-content-type]])
  (:require [compojure.route :as route]
            [ring.util.response :as resp]))

(defn pandoc-response [format content content-id]
  (let [html (pandoc format content)]
    (println format content)
    (println html)
    (if (zero? (:exit html))
      {:headers {"Content-Type" "Application/edn"}
       :body (str {:html (:out html)
                   :content-id content-id
                   :markup content})}
      {:status 400
       :body (:err html)})))

(defn markup-response [uri]
  (let [content (choose-file uri)]
    (if content
      {:body content
       :headers {"Content-Type" "text/plain"}})))

(def default-html (slurp "resources/public/index.html"))

(defn edn-request? [content-type]
  (and content-type
       (not (empty? (re-find #"application/(vnd.+)?edn" content-type)))))

(defroutes app-routes
  (route/resources "/")
  (GET "*" {content-type :content-type uri :uri} 
       (if (edn-request? content-type)
         (markup-response uri)
         default-html))
  (POST "/" [format content content-id] (pandoc-response format content content-id))
  (route/not-found "404"))

(def app
  (-> app-routes
      wrap-edn-params
      wrap-content-type))
