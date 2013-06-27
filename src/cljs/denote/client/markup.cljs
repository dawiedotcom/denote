(ns denote.client.markup)

(defn map->js [m]
  (let [out (js-obj)]
    (doseq [[k v] m]
      (aset out (name k) v))
    out))

(defn clear-value! [id]
  (let [node (.getElementById js/document id)]
    (set! (.-value node) "")))

(defn callback [html]
  (clear-value! "mdarea")
  (let [div (.createElement js/document "div")
        content (.getElementById js/document "content")]
    (set! (.-innerHTML div) html)
    (.appendChild content div)))

(defn postmd [md]
  (let [data (str "{:format :markdown :content \"" md "\"}")]
    ;(console.log data)
    (.ajax js/jQuery (map->js {:url "/"
                               :type "POST"
                               :contentType "application/edn"
                               :data data
                               :success callback}))))

(defn ^:export renderClicked []
  (let [mdarea (.getElementById js/document "mdarea")]
    (postmd (.-value mdarea))))
