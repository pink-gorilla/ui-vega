(ns pinkgorilla.gorilla-plot.goldly
  (:require

   [goldly.sci.bindings :refer [add-cljs-namespace add-cljs-bindings]]
   [systems.snippet-registry :refer [add-snippet]]))

; cljs ui


(add-cljs-namespace [pinkgorilla.gorilla-plot.pinkie])

(add-cljs-bindings {'listplot pinkgorilla.gorilla-plot.core/list-plot
                    'barchart pinkgorilla.gorilla-plot.core/bar-chart
                    'histogram pinkgorilla.gorilla-plot.core/histogram
                    'plot pinkgorilla.gorilla-plot.core/plot
                    'plot-compose pinkgorilla.gorilla-plot.core/compose

                    'timeseries-plot pinkgorilla.gorilla-plot.core/timeseries-plot
                    'multi-plot pinkgorilla.gorilla-plot.core/multi-plot})

(add-snippet {:type :goldly-clj
              :category :gorilla-plot
              :id :vega-gorilla-plot
              :filename "snippets/gorilla_plot/gorilla_plot.clj"})