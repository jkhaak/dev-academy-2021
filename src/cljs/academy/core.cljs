(ns academy.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r :refer [atom]]
            [reagent.dom :as rdom]
            [reagent-material-ui.colors :as colors]
            [reagent-material-ui.core.button :refer [button]]
            [reagent-material-ui.core.css-baseline :refer [css-baseline]]
            [reagent-material-ui.core.grid :refer [grid]]
            [reagent-material-ui.core.toolbar :refer [toolbar]]
            [reagent-material-ui.core.table :refer [table]]
            [reagent-material-ui.core.table-body :refer [table-body]]
            [reagent-material-ui.core.table-cell :refer [table-cell]]
            [reagent-material-ui.core.table-container :refer [table-container]]
            [reagent-material-ui.core.table-head :refer [table-head]]
            [reagent-material-ui.core.table-row :refer [table-row]]
            [reagent-material-ui.styles :as styles]))

(defn event-value
  [^js/Event e]
  (.. e -target -value))

(def custom-theme
  {:palette {:primary colors/purple
             :secondary colors/blue}})

(defn custom-styles [{:keys [spacing] :as theme}]
  {:button {:margin (spacing 1)}
   :text-field {:width 200
                :margin-left (spacing 1)
                :margin-right (spacing 1)}})

(def with-custom-styles (styles/with-styles custom-styles))

(defn custom-table-cell-style [{:keys [palette] :as theme}]
  {:head {:background-color (-> palette :common :black)
          :color (-> palette :common :white)}})

(defn custom-table-row-style [{:keys [palette] :as theme}]
  {:root {"&:nth-of-type(odd)" {:background-color (-> palette :action :hover)}}})

(def styled-table-cell ((styles/with-styles custom-table-cell-style) table-cell))
(def styled-table-row ((styles/with-styles custom-table-row-style) table-row))

(defonce data (atom nil))

(defn create-table-row [i {:keys [name amount]}]
  [styled-table-row
   {:key i}
   [styled-table-cell name]
   [styled-table-cell amount]])

(defn app [{:keys [classes] :as props}]
  [grid
   {:container true
    :direction "column"
    :spacing   2}

   [:h1 "Name app"]

   [grid {:item true}
    [toolbar
     {:disable-gutters true}
     [button
      {:variant  "contained"
       :class    (:button classes)

       :on-click
       (fn []
         (go (let [response (<! (http/get "http://localhost:3000/api/names/"))]
               (reset! data (-> response :body :names)))))}
      "Order by amount"]

     [button
      {:variant  "contained"
       :class    (:button classes)

       :on-click
       (fn []
         (go (let [response (<! (http/get "http://localhost:3000/api/names/alpha"))]
               (reset! data (-> response :body :names)))))}
      "Order by name"]]]

   (if @data
     [table-container
      [table
       {:class (:table classes)}
     
       [table-head
        [styled-table-row
         [styled-table-cell "Name"]
         [styled-table-cell "Amount"]]]

       [table-body
        (map-indexed create-table-row @data)]]])])


(defn root []
  [:<>
   [css-baseline]
   [styles/theme-provider (styles/create-mui-theme custom-theme)
    [grid
     {:container true
      :direction "row"
      :justify "center"}
     [grid
      {:item true
       :xs 6}
      [(with-custom-styles app)]]]]])

(defn ^:dev/after-load start []
  (js/console.log "start")
  (rdom/render [root] (.getElementById js/document "app")))

(defn ^:export init []
  (js/console.log "init")
  (start))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))
