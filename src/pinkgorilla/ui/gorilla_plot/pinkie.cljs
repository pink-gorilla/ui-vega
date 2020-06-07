(ns pinkgorilla.ui.gorilla-plot.pinkie
  (:require
   [pinkgorilla.ui.pinkie :as pinkie :refer-macros [register-component]]
   [pinkgorilla.ui.vega :refer [vega]]
   [pinkgorilla.ui.gorilla-plot.plot :as plot]
   [pinkgorilla.ui.gorilla-plot.core :refer [compose]]))

; performance tests

(defn ^{:category :demo}  xxx [state]
  [:p/leaflet
   (into [{:type :view :center [-16, 170.5] :zoom 4 :height 600 :width 700}]
         (for [{:keys [lat long]} (:quakes @state)]
           {:type :marker :position [lat long]}))])

(pinkie/register-component :p/xxx xxx)

; todo: move gorilla plot

; this wrappers are necessary, as the repl api for gorilla-plot 
; supplies options as partitioned :key :val args in the end
; reagent syntax typically passes options as a map in the
; first parameter.


(defn ^{:category :data} listplot
  ([data]
   (listplot {} data))
  ([options data]
   [vega (apply plot/list-plot data (apply concat options))]))

(defn ^{:category :data} barchart
  ([cat val]
   (barchart {} cat val))
  ([options cat val]
   [vega (apply plot/bar-chart cat val (apply concat options))]))

; histogram not yet working. the gorillaplot implementation uses way too
; much Math/xx unctions that are not defined in clojurescript.
(defn ^{:category :data} histogram
  ([data]
   (histogram {} data))
  ([options data]
   [vega (apply plot/histogram data (apply concat options))]))

(defn ^{:category :data} plot
  ([fun min-max]
   (plot {} fun min-max))
  ([options fun min-max]
   [vega (apply plot/plot fun min-max (apply concat options))]))

(defn sin [x]
  (.sin js/Math x))

(println "registering vega dsl plots .. ")
(register-component :p/listplot listplot)
(register-component :p/barchart barchart)
(register-component :p/histogram histogram)
(register-component :p/plot plot)
(register-component :p/composeplot compose) ; compose has problem with recursive pinkie.


