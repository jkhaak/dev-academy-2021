(ns server.core
  (:require [cheshire.core :refer [parse-string]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.util.http-response :as response]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.schema :as rcs]
            [reitit.ring :as ring]
            [schema.core :as s]))

(def names (-> (slurp "names.json")
               (parse-string true)
               :names))

(defn get-amount [names n]
  (-> (filter #(= (:name %) n) names)
      first
      :amount))

(defn sort-by-amount
  ([names]
   (sort-by-amount names true))
  ([names order]
   (let [ordering (if order < >)]
     (sort-by :amount ordering names))))
   
(defn sort-by-names
  ([names]
   (sort-by-names names false))
  ([names order]
   (let [ordering (if order reverse identity)]
     (ordering (sort-by :name names)))))

(defn calculate-total [names]
  (apply + (map :amount names)))

(def my-routes
  ["/api/names"
   ["/" {:get {:description "Return all names"
               :handler (fn [request]
                          (response/ok {:names (sort-by-amount names false)}))}}]
   ["/alpha" {:get {:description "Return all names in alphabetical order"
                    :handler (fn [request]
                               (response/ok {:names (sort-by-names names)}))}}]
   ["/total" {:get {:description "Return total amount"
                    :handler (fn [request]
                               (response/ok {:result (calculate-total names)}))}}]
   ["/amount/:name" {:get {:description "Return amount of a name"
                           :coercion rcs/coercion
                           :parameters {:path {:name s/Str}}
                           :handler (fn [request]
                                      (let [n (-> request :path-params :name)]
                                        (response/ok {:result (get-amount names n)})))}}]])

(def app
  (ring/ring-handler
    (ring/router
      [my-routes]
      {:data {:muuntaja   m/instance
              :middleware [params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))

(defonce running-server (atom nil))

(defn start []
  (when (nil? @running-server)
    (reset! running-server (jetty/run-jetty #'app {:port 3000
                                                   :join? false})))
  (println "Server running in port 3000"))

(defn stop []
  (when-not (nil? @running-server)
    (.stop @running-server)
    (reset! running-server nil))
  (println "Server stopped"))

(defn -main [& args]
  (println "hello world"))
