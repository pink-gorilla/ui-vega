(ns pinkgorilla.vega.goldly
  (:require
   [pinkgorilla.repl :refer [add-require]]
   [goldly.sci.bindings :refer [add-cljs-namespace add-cljs-bindings add-cljs-ns-bindings]]))

; vega renderer

(add-cljs-namespace [pinkgorilla.vega.pinkie]) ;cljs - pinkie
(add-require '[pinkgorilla.vega :refer [vega]])
(add-cljs-bindings {'vega pinkgorilla.vega/vega}) ; vega fn to render a spec

; plot ns in clj + cljs 

(add-require '[pinkgorilla.vega.plot.core :as plot])

(add-cljs-namespace [pinkgorilla.vega.plot.core])
(add-cljs-ns-bindings
 'plot {'list-plot pinkgorilla.vega.plot.core/list-plot
        'bar-chart pinkgorilla.vega.plot.core/bar-chart
        'histogram pinkgorilla.vega.plot.core/histogram
        'plot pinkgorilla.vega.plot.core/plot
        'compose pinkgorilla.vega.plot.core/compose
        'timeseries-plot pinkgorilla.vega.plot.core/timeseries-plot
        'multi-plot pinkgorilla.vega.plot.core/multi-plot})


