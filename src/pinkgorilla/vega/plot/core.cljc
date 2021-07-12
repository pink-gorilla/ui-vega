(ns pinkgorilla.vega.plot.core
  "functions in .plot ns will only generate vega-specs.
   In .core ns we wrap the result of all functions into [:vega spec]"
  (:require
   #?(:cljs [pinkgorilla.vega.impl.react :refer [vega vegalite]])
   [pinkgorilla.vega.plot.plot :as p]
   ;[pinkgorilla.vega.plot.multi :as m]
   ))

#?(:clj
   (defn- wrap [vega-spec]
     (with-meta
       [:p/vega vega-spec]
       {:R true}))

   :cljs
   (defn- wrap [vega-spec]
     [vega vega-spec]))

(defn- wrap-vega [fun]
  (fn [plot-spec]
    (wrap (fun plot-spec))))

(def plot (wrap-vega p/plot))
(def list-plot (wrap-vega p/list-plot))
(def bar-chart (wrap-vega p/bar-chart))
(def histogram (wrap-vega p/histogram))

(def timeseries-plot (wrap-vega p/timeseries-plot))
;(def multi-plot (wrap-vega m/multi-plot))

(defn -unwrap [renderable]
  (second renderable))

(defn ^{:category :data} compose
  [& plots]
  (let [plots-unwrapped (into [] (map -unwrap plots))]
    (wrap (apply p/compose plots-unwrapped))))

(comment

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
   (list-plot {:data d})
   (list-plot {:data d
               :plot-range [:all :all]
               :joined true
               :plot-size 400
               :aspect-ratio 1.6
               :opacity 0.5}))

  #?(:clj
     (defn sin [x] (Math/sin x)))

  (sin 8)

  (plot {:func sin
         :window [0 100]})

 ; 
  )

