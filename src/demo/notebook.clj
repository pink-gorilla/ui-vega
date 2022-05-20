(ns demo.notebook
  (:require
   [reval.document.notebook :refer [eval-notebook load-notebook create-notebook]]
   [goldly.scratchpad :refer [show! show-as clear!]]))

(def data
  {:table [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43} {:a "D" :b 91}
           {:a "E" :b 81} {:a "F" :b 53} {:a "G" :b 19} {:a "H" :b 87}
           {:a "I" :b 52} {:a "J" :b 127}]})

(def bar
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :mark {:type "bar"
          ;:tooltip true
          :tooltip {:content "data"}}
   :encoding {:x {:field "a" :type "ordinal"}
              :y {:field "b" :type "quantitative"}}
   :data {:name "table"}})

(show! ^:R
 ['user/vegalite {:spec bar :data data}])

(load-notebook "demo.notebook.vegalite-bar")

(create-notebook "demo.notebook.vegalite-bar")

(-> [:p/notebook (eval-notebook "demo.notebook.vegalite-bar")]
    show!)

(map eval-notebook ["demo.notebook.vegalite-arrow"
                    "demo.notebook.vegalite-bar"
                    "demo.notebook.vegalite-multiline"
                    "demo.notebook.vegalite-point"
                    "demo.notebook.vegalite-zoom"

                    "demo.notebook.vega-zoom"
                    "demo.notebook.vega-test"
                    "demo.notebook.vega-rect"

                    "demo.notebook.gorillaplot-core"])

