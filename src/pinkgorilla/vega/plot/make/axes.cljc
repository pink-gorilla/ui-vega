(ns pinkgorilla.vega.plot.make.axes)

(defn default-plot-axes
  [x-title y-title]
  {:axes [(merge {:orient "bottom" :scale "x"}
                 (when x-title {:title x-title :titleOffset 30}))
          (merge {:orient "left" :scale "y"}
                 (when y-title {:title y-title :titleOffset 45}))]})
