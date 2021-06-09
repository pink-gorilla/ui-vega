(ns pinkgorilla.vega.pinkie
  (:require
   [pinkie.pinkie :refer-macros [register-component]]
   [pinkgorilla.vega.impl.react :refer [vega vegalite]]
   [pinkgorilla.vega.plot.swings :refer [swingchart-spec]]))

;; vega


(register-component :p/vega vega)
(register-component :p/vegalite vegalite)

(defn swing-chart [{:keys [data] :as opts}]
  (let [opts (assoc opts :spec swingchart-spec)]
    [vegalite opts]))

(register-component :p/swing-chart swing-chart)