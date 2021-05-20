(ns pinkgorilla.gorilla-plot.goldly
  (:require
   [systems.snippet-registry :refer [add-snippet]]
   [goldly.sci.bindings :refer [add-cljs-namespace]]))

; cljs ui
(add-cljs-namespace [pinkgorilla.gorilla-plot.pinkie])

(add-snippet {:type :goldly-clj
              :category :gorilla-plot
              :id :vega-gorilla-plot
              :filename "snippets/gorilla_plot/gorilla_plot.clj"})