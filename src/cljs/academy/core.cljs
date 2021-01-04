(ns academy.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.dom :as rdom]))

(defn index []
  [:h1 "Dev Academy 2021 – Name App"])

(defn ^:dev/after-load start []
  (js/console.log "start")
  (rdom/render [index] (.getElementById js/document "app")))

(defn ^:export init []
  (js/console.log "init")
  (start))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))
