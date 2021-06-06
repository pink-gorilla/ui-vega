(ns pinkgorilla.vega)

(defn ^{:category :data
        :R true}
  vega
  "displays chart defined in vega spec
   "
  [vega-spec]
  ^:R
  [:p/vega vega-spec])
