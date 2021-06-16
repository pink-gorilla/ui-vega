(ns pinkgorilla.vega.pinkie
  (:require
   [pinkie.pinkie :refer-macros [register-component]]
   [pinkgorilla.vega.impl.react :refer [vega vegalite]]))

;; vega


(register-component :p/vega vega)
(register-component :p/vegalite vegalite)
