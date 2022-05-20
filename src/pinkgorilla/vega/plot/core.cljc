(ns pinkgorilla.vega.plot.core
  "functions in .plot ns will only generate vega-specs.
   In .core ns we wrap the result of all functions into [:vega spec]"
  (:require
   [pinkgorilla.vega.plot.plot :as p]
   ;[pinkgorilla.vega.plot.multi :as m]
   ))

;; wrap and unwrap


(defn- wrap [vega-spec]
  (with-meta
    ['user/vega
     vega-spec]
    {:R true}))


(defn -unwrap [renderable]
  (second renderable))

;; create wrapped versions

(defn- args->opts [args]
  (->> (partition 2 args)
       (map vec)
       (into {})))

(def plot (fn [func window & args]
            (-> (assoc (args->opts args) :func func :window window)
                p/plot
                wrap)))

(def list-plot (fn [data & args]
                 (-> (assoc (args->opts args) :data data)
                     p/list-plot
                     wrap)))

(def bar-chart (fn [categories values & args]
                 (-> (assoc (args->opts args) :categories categories :values values)
                     p/bar-chart
                     wrap)))

(def histogram  (fn [data & args]
                  (-> (assoc (args->opts args) :data data)
                      p/histogram
                      wrap)))

(def timeseries-plot (fn [data & args]
                       (-> (assoc (args->opts args) :data data)
                           p/timeseries-plot
                           wrap)))

;(def multi-plot (wrap-vega m/multi-plot))

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

