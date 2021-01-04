(ns academy.core
  (:require [reagent.core :as r :refer [atom]]
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

(defonce data (atom nil))

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
       :class    (:button classes)}
      "List names order by amount"]

     [button
      {:variant  "contained"
       :class    (:button classes)}
      "List names order by name"]]]
   [table-container
    [table
     {:class (:table classes)}
     
     [table-head
      [table-row
       [table-cell "#"]
       [table-cell "Name"]
       [table-cell "Amount"]]]

     [table-body
      [table-row
       [table-cell 1]
       [table-cell "Tuomas"]
       [table-cell "11"]]]]]])


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
