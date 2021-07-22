(ns pinkgorilla.vega.plot.make.marks)

(defn list-plot-marks
  [data-key color #_shape size opacity]
  {:marks [{:type        "symbol"
            :from       {:data data-key}
            :encode     {:enter  {:x           {:scale "x", :field "x"}
                                  :y           {:scale "y", :field "y"}
                                  :fill        {:value (or color "steelblue")}
                                  :fillOpacity {:value opacity}}
                         :update {:shape #_shape "circle"
                                  :size        {:value size}
                                  :stroke      {:value "transparent"}}
                         :hover  {:size   {:value (* 3 size)}
                                  :stroke {:value "white"}}}}]})

(defn line-plot-marks
  [data-key color opacity]
  {:marks [{:type       "line"
            :from       {:data data-key}
            :encode     {:enter {:x             {:scale "x", :field "x"}
                                 :y             {:scale "y", :field "y"}
                                 :stroke        {:value (or color "#FF29D2")}
                                 :strokeWidth   {:value 2}
                                 :strokeOpacity {:value opacity}}}}]})

;;; Bar charts

(defn bar-chart-marks
  [data-key color opacity]
  {:marks [{:type       "rect"
            :from       {:data data-key}
            :encode     {:enter {:x     {:scale "x", :field "x"}
                                 :width {:scale "x", :band 1, :offset -1}  ; :band true
                                 :y     {:scale "y", :field "y"}
                                 :y2    {:scale "y", :value 0}}
                         :update {:fill    {:value (or color "steelblue")}
                                  :opacity {:value opacity}}
                         :hover  {:fill {:value "#FF29D2"}}}}]})

;;; Histograms

(defn histogram-marks
  [data-key color opacity fillOpacity]
  {:marks [{:type       "line"
            :from       {:data data-key}
            :encode     {:enter {:x             {:scale "x", :field "x"}
                                 :y             {:scale "y", :field "y"}
                                 :interpolate   {:value "step-before"}
                                 :fill          {:value (or color "steelblue")}
                                 :fillOpacity   {:value fillOpacity}
                                 :stroke        {:value (or color "steelblue")}
                                 :strokeWidth   {:value 2}
                                 :strokeOpacity {:value opacity}}}}]})