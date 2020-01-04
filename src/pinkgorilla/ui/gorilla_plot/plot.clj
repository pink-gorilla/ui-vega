;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

(ns pinkgorilla.ui.gorilla-plot.plot
  (:require [pinkgorilla.ui.gorilla-plot.vega :as vega]
            [pinkgorilla.ui.gorilla-plot.util :as util]))

;; Series' are given random names so that plots can be composed
;; Thanks: https://gist.github.com/gorsuch/1418850


(defn- uuid [] (str (java.util.UUID/randomUUID)))

(defn add-indices [d] (map vector (range (count d)) d))

(defn list-plot
  "Function for plotting list data."
  [data & {:keys [joined plot-size aspect-ratio colour color plot-range #_symbol symbol-size opacity x-title y-title]
           :or   {joined       false
                  plot-size    400
                  aspect-ratio 1.618
                  plot-range   [:all :all]
                  ;;symbol       "circle"
                  symbol-size  70
                  opacity      1}}]
  (let [series-name (uuid)
        plot-data (if (sequential? (first data))
                    data
                    (add-indices data))]
    (merge
     (vega/container plot-size aspect-ratio)
     (vega/data-from-list series-name plot-data)
     (if joined
       (vega/line-plot-marks series-name (or colour color) opacity)
       (vega/list-plot-marks series-name (or colour color) #_symbol symbol-size opacity))
     (vega/default-list-plot-scales series-name plot-range)
     (vega/default-plot-axes x-title y-title))))

(defn plot
  "Function for plotting functions of a single variable."
  [func [xmin xmax] & {:keys [plot-points]
                       :or   {plot-points 100.0}
                       :as   opts}]
  (let [xs (range xmin xmax (float (/ (- xmax xmin) plot-points)))
        plot-data (map #(vector % (func %)) xs)]
    ;; surely there's a function to do this!
    (apply (partial list-plot plot-data) (mapcat identity (merge {:joined true} opts)))))

(defn bar-chart
  [categories values & {:keys [plot-size aspect-ratio colour color plot-range opacity x-title y-title]
                        :or   {plot-size    400
                               aspect-ratio 1.618
                               plot-range   [:all :all]
                               opacity      1}}]
  (let [series-name (uuid)]
    (merge
     (vega/container plot-size aspect-ratio)
     (vega/data-from-list series-name (map vector categories values))
     (vega/bar-chart-marks series-name (or colour color) opacity)
     (vega/default-bar-chart-scales series-name plot-range)
     (vega/default-plot-axes x-title y-title))))

(defn histogram
  "Plot the histogram of a sample."
  [data & {:keys [plot-range bins normalize normalise plot-size aspect-ratio colour color opacity fill-opacity x-title y-title]
           :or   {plot-range   [:all :all]
                  bins         :automatic
                  plot-size    400
                  aspect-ratio 1.618
                  opacity      1
                  fill-opacity  0.4}
           :as   opts}]
  (let [bin-range-spec (first plot-range)
        range-min (if (= bin-range-spec :all) (apply min data) (first bin-range-spec))
        range-max-raw (if (= bin-range-spec :all) (apply max data) (second bin-range-spec))
        ;; ensure the largest point is included
        ;; TODO: does this always work? With Clojure numeric types?
        range-max (+ range-max-raw (Math/ulp (double range-max-raw)))
        points-in-range (util/count-in-range data range-min range-max)
        ;; if bins :automatic then use the Sturges rule (it's simple)
        num-bins (if (= bins :automatic) (Math/ceil (+ 1 (/ (Math/log points-in-range) (Math/log 2)))) bins)
        bin-size-raw (/ (- range-max range-min) (double num-bins))
        ;; this is a hack to catch the case when all of the points are identical.
        ;; TODO: this could probably be done in a much nicer way.
        bin-size (if (< bin-size-raw 1e-15) 1.0 bin-size-raw)
        cat-counts (util/bin-counts data range-min range-max bin-size)
        ;; optionally normalise to probability - note that the normalisation is wrt the whole dataset, not just the
        ;; plotted portion.
        norm (case (or normalize normalise :count)
               :probability (count data)
               :probability-density (* (count data) bin-size)
               :count 1)
        cat-data (map #(/ % (double norm)) cat-counts)]
    (let [series-name (uuid)
          ;; we use a modified line plot to draw the histogram, rather than the more obvious bar-chart (as then the
          ;; scales are easier to work with, especially when adding lines). This requires jumping through some hoops:
          ;; move the x-points to be in the middle of their bins and add two extra
          x-data (map (partial + bin-size) (range (- range-min bin-size) (+ range-max bin-size) bin-size))
          ;; bookend the y-data with zeroes.
          y-data (concat [0] cat-data [0])
          plot-data (map vector x-data y-data)]
      (merge
       (vega/container plot-size aspect-ratio)
       (vega/data-from-list series-name plot-data)
       (vega/histogram-marks series-name (or colour color) opacity fill-opacity)
       (vega/default-list-plot-scales series-name plot-range)
       (vega/default-plot-axes x-title y-title)))))


;(defn from-vega
;  "extract vega-plot spec from pink-gorilla-repl output format.
;   all the individual plots are already formatted for pink-gorilla-repl
;   in order to compose them we have to first unwrap the gorilla format to get the naked
;   plot data, then they are composeable again"
;  [g]
;  (:content g))


(defn compose
  [& plots]
  (let [plot-data plots ;  (map from-vega plots)
        first-plot (first plot-data)
        {:keys [width height padding scales axes]} first-plot
        data (apply concat (map :data plot-data))
        marks (apply concat (map :marks plot-data))]
    {:width width
     :height height
     :padding padding
     :scales scales
     :axes axes
     :data data
     :marks marks}))

(comment
  (list-plot [1 2 3])

  (compose
   (list-plot [1 2 3])
   (list-plot [3 2 1])))