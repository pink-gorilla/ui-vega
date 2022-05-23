(ns demo.notebook.vegalite-point)

^:R
['ui.vega/vegalite
 {:spec {:data {:values [{:x 3 :y 4}
                         {:x 7 :y 1}
                         {:x 5 :y 7}
                         {:x 2 :y 6}]}
         :mark :point
         :encoding {:x {:field :x :type :quantitative}
                    :y {:field :y :type :quantitative}}}}]


