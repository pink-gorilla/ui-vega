
(ns demo.notebook.gorilla-plot.tableview
  (:require
   [pinkgorilla.vega.plot.core :refer [list-plot bar-chart compose histogram plot]]))

(defn make-row
  [order]
  [order [:p/math (str "x^{" order "}")] (plot (fn [x] (Math/pow x order)) [0 10] :plot-size 250)])

; visualize one row
^:R [:p/gtable [(make-row 3)]]

^:R [:p/gtable  (into [[[:span "Order"] [:p "Form"] [:h1 "Plot"]]] (vec (map make-row (range 1 5))))]

(defn circ [col] ^:R [:svg {:height 100 :width 100}
                      [:circle {:cx 50 :cy 50 :r 40 :stroke "black" :stroke-width 4 :fill col}]])

(circ "green")

^:R [:p/gtable  (into [[1 2 3 4 5]] (vec (repeat 5 (map circ ["red" "green" "blue" "yellow" "orange"]))))]

