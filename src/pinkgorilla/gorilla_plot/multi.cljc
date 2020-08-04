(ns pinkgorilla.gorilla-plot.multi
  "multi-plots
     -multiple plots in one chart
     -multiple plots horizontal"
  (:require
   #?(:clj  [clj-time.core :as t]
      :cljs [cljs-time.core :as t])
   #?(:clj  [clj-time.coerce :as tc]
      :cljs [cljs-time.coerce :as tc])))

(defn container-lite
  "vega-lite container (version 4)"
  [];plot-size aspect-ratio]
  {:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   ;:width   plot-size
   ;:height  (float (/ plot-size aspect-ratio))
   ;:padding {:top 10, :left 55, :bottom 40, :right 10}
   })

(defn make-time-axis [size]
  (let [start-dt (t/plus (t/today-at 12 00) (t/months (* -1 size)))]
    (map
     (fn [idx]
       (tc/to-long (t/plus start-dt (t/months (+ idx 1)))))
     (range size))))

(defn make-linear-axis [size]
  (range size))

(defn convert-series
  "converts a series [vector of number]
   to a vega data spec with name"
  [series time]
  (let [;time can be nil, if so we build a range as x axis
        time-sanitized (if time time (make-time-axis (count series)))]
    {:values (into [] (map
                       (fn [x y] {:x x :y y})
                       time-sanitized series))}))

(defn build-series-rect
  "builds a vega lite plot-spec for a single series"
  [width _ #_time _ #_last? m]
  (let [{:keys [data height]} m]
    (merge
     (when height {:height height})
     (when width {:width width})
     {:mark {:type "rect"}
      :data {:values data}
      :encoding {:x {:field "start"
                     :type "temporal"}
                 :x2 {:field "end"}
                 :color {:field "color"} ; {:value color}
                 }})))

(defn build-series-all
  "builds a vega lite plot-spec for a single series"
  [width time last? m]
  ;(println "build-series " m)
  (let [;name (uuid)
        {:keys [data color title orient height #_width type scale zero?]
         :or {color "#85C5A6"
              title nil
              orient "left"
              height nil
              ;width nil
              type "point"
              scale "linear"
              zero? true}} m]
    (merge
     (when height {:height height})
     (when width {:width width})

     {:mark {:type type :color color}
      :data (convert-series data time)
      :encoding {:x {:field "x"
                     ;:type "quantitative"
                     :type "temporal"
                     :axis {:title  ""
                            :labels last? ; only last timeseries will show x-axis labels
                            }}
                 :y (merge
                     (when title
                       {:axis {:title title :titleColor "black" :orient orient}})
                     (when scale
                       {:scale {:type scale
                                :zero zero?}})
                     {:field "y"
                      :type "quantitative"})}})))

(defn build-series
  [width time last? m]
  (let [type (:type m)]
    (case type
      :rect (build-series-rect width time last? m)
      "rect" (build-series-rect width time last? m)
      (build-series-all width time last? m))))

(defn- build-plot
  "builds a plot that can contain one or more series"
  [width time last? series]
  (if (vector? series)
    (if (> (count series) 1)
      ; multiple series on one plot
      {:layer (into [] (map (partial build-plot width time last?) series))
       :resolve {:scale {:y "independent"}}}
      ;list of series, but only one plot
      (build-series width time last? (first series)))
    ; plot with only single series
    (build-series width time last? series)))

(defn- build-plots
  "builds one or more plots.
   multiple plots are organized vertically"
  [width time & plots]
  (if (= (count plots) 1)
    (build-plot width time true (first plots)) ; no vertical plots
    ; vconcat multiple plots
    {:vconcat (conj
               (into [] (map (partial build-plot width time false) (butlast plots)))
               (build-plot width time true (last plots)))
     ;:resolve {:scale {:y "independent"}}
     }))

(defn multi-plot
  "plots one or more timeseries plots (stacked vertically)
   input: [options vector-of-plots]

   options
   - is optional
   - if specified, a map with optional keys:
     :width (pixels)
     :time (time data as unix-long)

   a plot contains either 
   - one series (map)
   - more series (vector of maps)

   a series is a map
   - mandatory keys:
      :data - vector of (length n)
   - optional keys:
      :color
      :orient   :left :right
      :title   (of the axis)    
      :height
      :width

   example:
   (def a [1 2 4 3 2])
   (def b [-1 1 -2 3 0])
   (def c [6 5 1 7 5])

   (multi-plot {:width 100} 
      {:data c :orient :left :title \"C\" :color \"blue\" :height 20 } 
      [{:data a :orient :right :title \"A\" :color \"red\" :height 50} 
      {:data b :orient :left :title \"B\" :height 50}])) 
  
   note that the first plot is single series (therefore a map), 
   the second plot has 2 series (vector of maps)
 "
  [& plots]
  (let [; optional parameter plot settings as map in first parameter
        [args plots] (if (map? (first plots))
                       [(first plots) (rest plots)]
                       [nil plots])
        ;_ (println "args: " args)
        ;_ (println "plots: " plots)
        {:keys [width time]
         :or   {width    800
                time nil}} args]
    (merge
     (container-lite); plot-size aspect-ratio)
     (apply (partial build-plots width time) plots))))

(comment

  (def a [1 2 4 3 2])
  (def b [-1 1 -2 3 0])
  (def c [6 5 1 7 5])

  (convert-series a nil)
  (build-series 500 nil true {:data a :orient "right"})

  (vector? {:data a :orient "right"})

  (build-plot 500 nil true {:data a :orient "right"})
  (build-plot 500 nil true [{:data a :orient "right" :title "A"}
                            {:data b :orient "left" :title "B"}])
  ;; => {:layer [{:width 500, :mark {:type "point", :color "#85C5A6"}, :data {:values [{:x 0, :y 1} {:x 1, :y 2} {:x 2, :y 4} {:x 3, :y 3} {:x 4, :y 2}]}, :encoding {:x {:field "x", :type "quantitative", :axis {:title "", :labels true}}, :y {:axis {:title "A", :titleColor "black", :orient "right"}, :field "y", :type "quantitative"}}} {:width 500, :mark {:type "point", :color "#85C5A6"}, :data {:values [{:x 0, :y -1} {:x 1, :y 1} {:x 2, :y -2} {:x 3, :y 3} {:x 4, :y 0}]}, :encoding {:x {:field "x", :type "quantitative", :axis {:title "", :labels true}}, :y {:axis {:title "B", :titleColor "black", :orient "left"}, :field "y", :type "quantitative"}}}], :resolve {:scale {:y "independent"}}}


  (build-plots 500 {:data a :orient "right" :title "A"}
               {:data b :orient "left" :title "B"})

  (clojure.repl/doc multi-plot)

  (multi-plot  [{:data a :orient "right" :title "A"}
                {:data b :orient "left" :title "B"}])

  (multi-plot {:width 1000}
              [{:data c :orient "left" :title "C" :height 500 :scale "log"}]
              [{:data a :orient "right" :title "A"}
               {:data b :orient "left" :title "B"}]))

