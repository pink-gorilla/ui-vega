(ns pinkgorilla.vega.plot.compact
  (:require
   [pinkgorilla.vega.plot.plot :as plot]
   [pinkgorilla.vega.plot.core :refer [compose]]))


; this wrappers are necessary, as the repl api for gorilla-plot 
; supplies options as partitioned :key :val args in the end
; reagent syntax typically passes options as a map in the
; first parameter.


(defn ^{:category :data
        :R true} list-plot
  ([data]
   (list-plot {} data))
  ([options data]
   [:p/vega (apply plot/list-plot data (apply concat options))]))

(defn ^{:category :data :R true} bar-chart
  ([cat val]
   (bar-chart {} cat val))
  ([options cat val]
   [:p/vega (apply plot/bar-chart cat val (apply concat options))]))

; histogram not yet working. the gorillaplot implementation uses way too
; much Math/xx functions that are not defined in clojurescript.
(defn ^{:category :data :R true} histogram
  ([data]
   (histogram {} data))
  ([options data]
   [:p/vega (apply plot/histogram data (apply concat options))]))

(defn ^{:category :data :R true} plot
  ([fun min-max]
   (plot {} fun min-max))
  ([options fun min-max]
   [:p/vega (apply plot/plot fun min-max (apply concat options))]))
