(ns pinkgorilla.ui.gorilla-plot.plot-test
  (:require
   #?(:cljs [cljs.test :refer-macros [deftest is]]
      :clj  [clojure.test :refer [deftest is]])
   [pinkgorilla.ui.gorilla-plot.core :refer [list-plot]]))

(def d
  [1 3 5 7 9 5 4 6 9 8 3 5 6])

(deftest list-plot-test
  (let [p (list-plot d)]
    (is (= :p/vega (first p)))))

; todo: 
; bar-chart compose histogram plot

