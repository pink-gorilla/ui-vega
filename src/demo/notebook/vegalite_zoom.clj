(ns demo.notebook.vegalite-zoom)

^:R
['ui.vega/vegalite
 {:spec {:data {:url "/r/data/sp500.csv"}
         :vconcat
         [{;:width 480
           :mark "area"
           :encoding {:x {:field "date"
                          :type "temporal"
                          :scale {:domain {:selection "brush"}}
                          :axis {:title ""}}
                      :y {:field "price", :type "quantitative"}}}
          {;:width 480
           :height 60
           :mark "area"
           :selection
           {:brush {:type "interval", :encodings ["x"]}}
           :encoding {:x {:field "date", :type "temporal"}
                      :y
                      {:field "price"
                       :type "quantitative"
                       :axis {:tickCount 3, :grid false}}}}]}}]