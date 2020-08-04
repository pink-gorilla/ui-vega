(ns pinkgorilla.gorilla-plot.pinkie-test
  (:require
   #?(:cljs [cljs.test :refer-macros [deftest is]]
      :clj  [clojure.test :refer [deftest is]])
   [pinkgorilla.gorilla-plot.pinkie]))

; make sure it compiles


(deftest pinkie-compile
  (is (= 0 0)))