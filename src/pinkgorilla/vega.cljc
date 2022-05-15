(ns pinkgorilla.vega)

(defn ^{:category :data
        :R true}
  vega
  "displays chart defined in vega spec
   "
  [vega-spec data]
  ^:R
  ['user/vega vega-spec data])

(defn ^{:category :data
        :R true}
  vegalite
  "displays chart defined in vega spec
   "
  [vega-spec data]
  ^:R
  ['user/vegalite vega-spec data])

(def vega-themes
  [nil
   "dark"
   "excel"
   "quartz"
   "vox"
   "fivethirtyeight"
   "latimes"
   "googlecharts"])

(def vega-opts
  {:theme  "quartz" ; "ggplot2"
   :tooltip {:theme "dark"}
   :hover     true  ; enable hover event processing
   :width 600
   :height 600
   :padding 5})