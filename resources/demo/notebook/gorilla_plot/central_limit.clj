
;;; # Central limit theorem
;;; We're going to look at the CLT in this worksheet, and simulate its action. Be reminded that
;;; $$ \sum_i u_i \overset{d}\to N $$
;;; where @@u_i@@ is a uniformly distributed random variable.

(ns demo.notebook.gorilla-plot.central-limit
  (:require
   [pinkgorilla.vega.plot.core :refer [list-plot bar-chart compose histogram plot]]))

^:R [:p/math " \\sum_i u_i \\overset{d}\\to N "]

(defn r [] (- (apply + (repeatedly 50 rand)) 25))

(def data (repeatedly 1000 r))

;;; Let's compare the simulated data and the expected distribution.

(defn gaussian
  [x sigma mu]
  (* (/ 0.4 sigma) (Math/exp (- (/ (* (- x mu) (- x mu)) (* 2 (* sigma sigma)))))))

(compose
 (histogram data :bins 50 :normalise :probability-density)
 (plot #(gaussian % 2 0) [-10 10]))

