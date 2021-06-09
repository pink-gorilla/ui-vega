(ns pinkgorilla.vega.plot.make.container)

;; Constants for padding and offsets are chosen so
;; that simple axis titles are visible and do not
;; obstruct axis labels, for common ranges. A smarter
;; dynamic approach is probably possible, but for most
;; practical cases this is sufficient.

(defn container-vega
  [plot-size aspect-ratio]
  {:$schema "https://vega.github.io/schema/vega/v5.json"
   :width   plot-size
   :height  (float (/ plot-size aspect-ratio))
   :padding {:top 10, :left 55, :bottom 40, :right 10}})

(defn data-from-list
  [data-key data]
  {:data [{:name   data-key
           :values (into []
                         (map (fn [[x y]] {:x x :y y}) data))}]})



