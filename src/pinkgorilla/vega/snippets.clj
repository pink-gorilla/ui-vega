(ns pinkgorilla.vega.snippets
  (:require
   [systems.snippet-registry :refer [add-snippet]]))


;; vega


(add-snippet {:type :pinkie
              :category :vega
              :id :vega
              :filename "snippets/vega/vega.edn"})

(add-snippet {:type :goldly-clj
              :category :vega
              :id :vega-bar
              :filename "snippets/vega/bar.clj"})

(add-snippet {:type :pinkie
              :category  :vega
              :id :vega-multiline
              :filename "snippets/vega/multiline.edn"})

(add-snippet {:type :pinkie
              :category  :vega
              :id :vega-zoom
              :filename "snippets/vega/zoom.edn"})

(add-snippet {:type  :goldly-clj
              :category  :vega
              :id :vega-combo
              :filename "snippets/vega/vega_combo.clj"})

;; plot

(add-snippet {:type :goldly-clj
              :category :vega-plot
              :id :vega-gorilla-plot
              :filename "snippets/vega.plot/gorilla_plot.clj"})