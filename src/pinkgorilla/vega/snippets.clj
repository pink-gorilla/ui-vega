(ns pinkgorilla.vega.snippets
  (:require
   [systems.snippet-registry :refer [add-snippet]]))

;vegalite

(add-snippet {:type :pinkie
              :category :vega
              :id :vegalite-point
              :filename "snippets/vegalite/point.edn"})

(add-snippet {:type :goldly-clj
              :category :vega
              :id :vegalite-bar
              :filename "snippets/vegalite/bar.clj"})

(add-snippet {:type :goldly-clj
              :category :vega
              :id :vegalite-bar-scroll
              :filename "snippets/vegalite/bar_scroll.clj"})

(add-snippet {:type :pinkie
              :category  :vega
              :id :vegalite-zoom
              :filename "snippets/vegalite/zoom.edn"})

(add-snippet {:type :pinkie
              :category  :vega
              :id :vegalite-multiline
              :filename "snippets/vegalite/multiline.edn"})


;; vega


(add-snippet {:type :pinkie
              :category :vega
              :id :vega-rect
              :filename "snippets/vega/rect.edn"})

(add-snippet {:type :pinkie
              :category :vega
              :id :vega-zoom
              :filename "snippets/vega/zoom.edn"})

(add-snippet {:type  :goldly-clj
              :category  :vega
              :id :vega-combo
              :filename "snippets/vega/combo.clj"})

;; plot

; broken
#_(add-snippet {:type :goldly-clj
                :category :vega-plot
                :id :vega-gorilla-plot
                :filename "snippets/vega.plot/gorilla_plot.clj"})

(add-snippet {:type :pinkie
              :category :vega-plot
              :id :swing-chart
              :filename "snippets/vega.plot/swings.edn"})