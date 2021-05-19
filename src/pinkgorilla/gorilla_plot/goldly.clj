(ns pinkgorilla.gorilla-plot.goldly
  (:require
   [taoensso.timbre :as timbre :refer [info warn error]]
   [goldly.sci.bindings :refer [add-cljs-namespace add-cljs-bindings generate-bindings]]))

; cljs ui
(add-cljs-namespace [pinkgorilla.gorilla-plot.pinkie])
