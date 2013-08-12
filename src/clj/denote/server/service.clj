(ns denote.server.service
  (:use compojure.core
        [denote.server.pandoc :only [pandoc]]
        [denote.server.io :only [choose-file]]
        [ring.middleware.edn :only [wrap-edn-params]]
        [ring.middleware.content-type :only [wrap-content-type]])
  (:require [compojure.route :as route]
            [clojure.java.io]
            [ring.util.response :as resp]
            [clojure.string :as string]))

(defn pandoc-response [format content content-id]
  (let [html (pandoc format content)]
    (if (zero? (:exit html))
      {:headers {"Content-Type" "Application/edn"}
       :body (str {:html (:out html)
                   :content-id content-id
                   :markup content})}
      {:status 400
       :body (:err html)})))

(defn content->response [content]
  (let [snippets (string/split content #"\n[\ \n]*\n")
        pandoc-result (map #(pandoc :markdown %) snippets)
        html-snippets (map :out pandoc-result)]
    (map (fn [& args] (zipmap '(:par :markup :html) args))
         (range)
         snippets
         html-snippets)))

(defn markup-response [uri]
  (let [{content :content :as body} (choose-file uri)]
    (if content
      {:body (->> content
                  content->response 
                  (assoc body :content ,,,)
                  str)
       :headers {"Content-Type" "application/edn"}})))

(def default-html (slurp (clojure.java.io/resource "public/index.html")))

(defn edn-request? [content-type]
  (and content-type
       (not (empty? (re-find #"application/(vnd.+)?edn" content-type)))))

(defn markup-str [markup-map]
  (let [pars (map second (sort-by first markup-map))]
    (apply str (interleave pars (repeat "\n\n")))))

(defroutes app-routes
  (route/resources "/")
  (GET "*" {content-type :content-type uri :uri} 
       (if (edn-request? content-type)
         (markup-response uri)
         (slurp (clojure.java.io/resource "public/index.html"))));default-html))
  (POST "/" [format content content-id] (pandoc-response format content content-id))
  (POST "/save" [uri ext markup] (spit (str "." uri "." ext) (markup-str markup)))
  (route/not-found "404"))

(def app
  (-> app-routes
      wrap-edn-params
      wrap-content-type))
