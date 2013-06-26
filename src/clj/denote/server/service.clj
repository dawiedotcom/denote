(ns denote.server.service
  (:gen-class)
  (:use compojure.core
        [denote.server.pandoc :only [pandoc]]
        [ring.middleware.edn :only [wrap-edn-params]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))


(defn pandoc-response [format content]
  (let [html (pandoc format content)]
    (println format content)
    (println html)
    (if (zero? (:exit html))
      {:body (:out html)}
      {:status 400
       :body (:err html)})))

(defroutes app-routes
  (GET "/" [] "<h1> hello </h1>")
  (POST "/" [format content] (pandoc-response format content))
  (route/not-found "404"))

(def app
  (-> app-routes
      wrap-edn-params))
  


