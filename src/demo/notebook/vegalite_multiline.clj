(ns demo.notebook.vegalite-multiline)

^:R
['ui.vega/vegalite
 {:width 1400
  :height 600
  :spec {:description "Stock prices of 5 Tech Companies over Time."
         :data {:url "/r/data/stocks.csv"}
         :transform [;{:filter "datum.symbol==='GOOG'"},
                     {:filter {:field "date", :timeUnit "year", :range [2007, 2010]}}]
         :mark "line"
         :selection   {:brush {:type "interval"}} ; :encodings ["x"]
         :encoding  {;:row {:field "symbol" :type "nominal"}
    ;:x {:field "date", :type "temporal"   :scale {:domain {:selection "brush"}}}
                     :x {:field "date" :type "temporal"
                         :axis {:tickCount 8
                                :labelAlign "left"
                                :labelExpr "[timeFormat(datum.value, '%b'), timeFormat(datum.value, '%m') == '01' ? timeFormat(datum.value, '%Y') : '']"
                                :labelOffset 4
                                :labelPadding -24
                                :tickSize 30
                                :gridDash {:condition {:test {:field "value" :timeUnit "month", :equal 1}, :value []}
                                           :value [2,2]}
                                :tickDash {:condition {:test {:field "value", :timeUnit "month", :equal 1}, :value []}
                                           :value [2,2]}}}
                     :y {:field "price", :type "quantitative"}
                     :color {:field "symbol", :type "nominal"}}}}]
