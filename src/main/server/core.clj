(ns server.core
  (:require [cheshire.core :refer [parse-string]]))

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

(defn -main [& args]
  (println "hello world"))
