(ns pinkgorilla.vega.plot.plot
  (:require
   [pinkgorilla.vega.plot.make.container :refer [container-vega data-from-list]]
   [pinkgorilla.vega.plot.make.marks :as marks]
   [pinkgorilla.vega.plot.make.axes :as axes]
   [pinkgorilla.vega.plot.make.scales :as scales]
   [pinkgorilla.vega.plot.util :as util :refer [gen-uuid]]))

;; Series' are given random names so that plots can be composed
;; Thanks: https://gist.github.com/gorsuch/1418850
(defn merge-with-meta [& data]
  (let [metas (map meta data)
        merged-meta (apply merge metas)]
    (with-meta (apply merge data) merged-meta)))

(defn add-indices [d] (map vector (range (count d)) d))

(defn list-plot
  "Function for plotting list data."
  [{:keys [data plot-range series-name
           joined
           plot-size aspect-ratio symbol-size opacity  ; keys with default values
           color x-title y-title] ; keys  without default values
    :or   {joined       false
           plot-size    400
           aspect-ratio 1.618
           plot-range   [:all :all]
           symbol-size  70
           opacity      1
           series-name (gen-uuid)
                  ;;symbol       "circle"
           }}]
  (let [plot-data (if (sequential? (first data))
                    data
                    (add-indices data))]
    (merge-with-meta
     (container-vega plot-size aspect-ratio)
     (data-from-list series-name plot-data)
     (if joined
       (marks/line-plot-marks series-name color opacity)
       (marks/list-plot-marks series-name color #_symbol symbol-size opacity))
     (scales/default-list-plot-scales series-name plot-range)
     (axes/default-plot-axes x-title y-title))))

(defn timeseries-plot [data & keys]
  (let [series-name (gen-uuid)
        params (into [] (concat [data :series-name series-name] keys))
        plot (apply list-plot params)
        plot-range [:all :all]]
    (merge-with-meta
     plot
     (scales/timeseries-list-plot-scales series-name plot-range))))

(defn plot
  "Function for plotting functions of a single variable."
  [{:keys [func window plot-points]
    :or   {plot-points 100.0}
    :as   opts}]
  (let [[xmin xmax] window
        opts (merge {} opts)
        xs (range xmin xmax (float (/ (- xmax xmin) plot-points)))
        plot-data (map #(vector % (func %)) xs)]
    ;; surely there's a function to do this!
    (println "data" plot-data)
    (list-plot (merge opts {:data plot-data
                            :joined true}))))

(defn bar-chart
  [{:keys [categories values
           plot-size aspect-ratio plot-range opacity ; keys with defaults
           color x-title y-title]
    :or   {plot-size    400
           aspect-ratio 1.618
           plot-range   [:all :all]
           opacity      1}}]
  (let [series-name (gen-uuid)]
    (merge-with-meta
     (container-vega plot-size aspect-ratio)
     (data-from-list series-name (map vector categories values))
     (marks/bar-chart-marks series-name color opacity)
     (scales/default-bar-chart-scales series-name plot-range)
     (axes/default-plot-axes x-title y-title))))

(defn histogram
  "Plot the histogram of a sample."
  [{:keys [data plot-range
           bins normalize normalise
           plot-size aspect-ratio color opacity fill-opacity x-title y-title]
    :or   {plot-range   [:all :all]
           bins         :automatic
           plot-size    400
           aspect-ratio 1.618
           opacity      1
           fill-opacity 0.4}}]
  (let [bin-range-spec (first plot-range)
        range-min (if (= bin-range-spec :all) (apply min data) (first bin-range-spec))
        range-max-raw (if (= bin-range-spec :all) (apply max data) (second bin-range-spec))
        ;; ensure the largest point is included
        ;; TODO: does this always work? With Clojure numeric types?
        range-max (+ range-max-raw ;(Math/ulp 
                     (double range-max-raw));)
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
        cat-data (map #(/ % (double norm)) cat-counts)
        series-name (gen-uuid)
        ;; we use a modified line plot to draw the histogram, rather than the more obvious bar-chart (as then the
        ;; scales are easier to work with, especially when adding lines). This requires jumping through some hoops:
        ;; move the x-points to be in the middle of their bins and add two extra
        x-data (map (partial + bin-size) (range (- range-min bin-size) (+ range-max bin-size) bin-size))
        ;; bookend the y-data with zeroes.
        y-data (concat [0] cat-data [0])
        plot-data (map vector x-data y-data)]
    (merge-with-meta
     (container-vega plot-size aspect-ratio)
     (data-from-list series-name plot-data)
     (marks/histogram-marks series-name color opacity fill-opacity)
     (scales/default-list-plot-scales series-name plot-range)
     (axes/default-plot-axes x-title y-title))))

(defn compose
  [& plots]
  (let [first-plot (first plots)
        {:keys [width height padding scales axes]} first-plot
        data (apply concat (map :data plots))
        marks (apply concat (map :marks plots))]
    {; take plot parameter from first plot
     :width   width
     :height  height
     :padding padding
     :scales  scales
     :axes    axes
     ; merge data and marks
     :data    data
     :marks   marks}))

(comment

  (meta (merge-with-meta
         ^:R {:a 1 :b 2}
         ^:r {:c 3 :d 4}))

  (def d [1 3 5 7 9 5 4 6 9 8 3 5 6])

  (list-plot {:data d})

  (list-plot {:data d
              :plot-range [:all :all]
              :joined true
              :plot-size 400
              :aspect-ratio 1.6
              :opacity 0.5})

  (bar-chart {:categories (range (count d))
              :values d})

  (histogram {:data d})

  (compose
   [list-plot {:data d}]
   [list-plot {:data d
               :plot-range [:all :all]
               :joined true
               :plot-size 400
               :aspect-ratio 1.6
               :opacity 0.5}])

  #?(:clj
     (defn sin [x] (Math/sin x)))

  (sin 8)

  (plot {:func sin
         :window [0 100]})

 ; 
  )