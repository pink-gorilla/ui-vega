
(def bar
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
  ;:autosize {:type "fit"}
  ; :overflow "auto"
   :width {:step 60
           :type "container"}
   :height 400
   :mark {:type "bar"
          ;:tooltip true
          :tooltip {:content "data"}}
   :encoding {:x {:field "a" :type "ordinal"}
              :y {:field "b" :type "quantitative"}}
   :data {:name "table"}})

(def data
  {:table [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43} {:a "D" :b 91}
           {:a "E" :b 81} {:a "F" :b 53} {:a "G" :b 19} {:a "H" :b 87}
           {:a "I" :b 52} {:a "J" :b 127}
           {:a "K" :b 28} {:a "L" :b 55} {:a "M" :b 43} {:a "N" :b 91}
           {:a "O" :b 81} {:a "P" :b 53} {:a "Q" :b 19} {:a "R" :b 87}
           {:a "S" :b 52} {:a "T" :b 127}]})

[:div {:style {:width 400
               :height 500
               :overflow "hidden"
               :background-color "green"}}
 [:p/vegalite {:spec bar :data data}]]


