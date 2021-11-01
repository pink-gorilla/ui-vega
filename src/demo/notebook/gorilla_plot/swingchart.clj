(ns demo.notebook.gorilla-plot.swingchart)
(require '[pinkgorilla.repl :refer [require-default]])
(require '[pinkgorilla.repl.clj.pomegranate :refer [add-dependencies]])
 ; (add-dependencies '[cljs-ajax/cljs-ajax "0.8.3"]) 
(require-default)

^:R [:p.bg-blue-300 "Hello, World!"]

(plot/list-plot (concat (range 10) (reverse (range 10))))

(vega {:$schema "https://vega.github.io/schema/vega-lite/v5.json",
       :description "Shows the relationship between horsepower and the number of cylinders using tick marks.",
       :data {:url "/r/data/cars.json"},
       :height 400
       :width 500
       :mark "tick",
       :encoding {"x" {:field "Horsepower", :type "quantitative"},
                  "y" {:field "Cylinders", :type "ordinal"}}})

(vega {;:$schema "http://localhost:8000/r/vega-lite/build/vega-lite-schema.json"  ;https://vega.github.io/schema/vega-lite/v5.json",
       :description "Shows the relationship between horsepower and the number of cylinders using tick marks.",
       :data {:url "/r/data/swings.json"},
       :height 400
       :width 500
       :layer [{:mark "rect" ; {:type "rect", :height 100 :width 200}
                :encoding {"x"   {:field "idx"  :type "quantitative"},
                           "x2"  {:field "idx2"  :type "quantitative"}
                           "y"   {:field "Low"  :type "quantitative"}
                           "y2"  {:field "High" :type "quantitative"}
                            ;"color" {:value  "orange"}
                           "color" {:field "dir"
                                    :type "nominal"
                                    :scale {:domain ["up" "down"],
                                            :range ["green" "#c7c7c7"]}}}}

               {:mark {:type "point", :height 10 :width 20}
                :encoding {"x"   {:field "idx", :type "quantitative"},
                           "y"   {:field "High", :type "quantitative"}
                           "color" {:value  "red"}}}]})

(plot/list-plot [7 8 6 7 8 9 4 5])

^:R [:p/list-plot {:joined true
                   :plot-size 400
                   :color "red"
                   :aspect-ratio 1.6
                   :plot-range [:all :all]
                   :opacity 0.5} [7 8 7 6 5 8]]

(defn rand-plot []
  (plot/list-plot (repeatedly 30 #(rand 10)) :joined true))
(rand-plot)

(grid {:cols 5 :background-color "blue"} (repeatedly 20 rand-plot))


