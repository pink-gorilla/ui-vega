(ns pinkgorilla.gorilla-plot.pinkie
  (:require
   [pinkie.pinkie :refer-macros [register-component]]
   [pinkgorilla.ui.viz.vega :refer [vega]]
   [pinkgorilla.gorilla-plot.plot :as plot]
   [pinkgorilla.gorilla-plot.core :refer [compose]]))

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
; much Math/xx functions that are not defined in clojurescript.
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

(register-component :p/listplot listplot)
(register-component :p/barchart barchart)
(register-component :p/histogram histogram)
(register-component :p/plot plot)
(register-component :p/composeplot compose) ; compose has problem with recursive pinkie.


