(ns pinkgorilla.vega.plot.latex
  (:require
   [clojure.string :as str]))

(defn latexify
  [expr]
  (if (seq? expr)
    (case (first expr)
      +  (str/join " + " (map latexify (rest expr)))
      *  (str/join " " (map latexify (rest expr)))
      ** (str (latexify (first (rest expr))) "^{" (latexify (second (rest expr))) "}"))
    (pr-str expr)))