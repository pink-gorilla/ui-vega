(ns ui.vega.impl.js
  "plugin to render vega-charts in pink-gorilla"
  (:require
   [pinkie.jsrender :refer [render-js]]
   ["vega-embed" :as vega-embed]))

;; NOTES:
;; Vega-Embed depends on Vega-Lite and Vega
;; Vega-Lite depends on Vega
;; The load order has to be VEGA, VEGA-LITE, VEGA-EMBED.

;; https://github.com/vega/vega-embed/issues/8
;; https://github.com/biocore/qurro/commit/baf8542bd08dfdb5a078bca3f73cddbd79faef93

; for vega embedding options see:  https://github.com/vega/vega-embed

(def vega-options {:actions false
                   :defaultStyle true})

(def vega-options-js (clj->js vega-options))

(defn render-vega [dom-node data-js]
  (-> (vega-embed dom-node data-js vega-options-js)
      (.catch (fn [em]
                (println "Error in Rendering Vega Spec: "  em);
                (.appendChild dom-node
                              (.createTextNode js/document (str "Vega Spec error: " em)))))))

; [cljsjs.vega-tooltip]
; https://github.com/sorenmacbeth/vizard/blob/master/src/cljs/vizard/core.cljs

#_(defn parse-vl-spec [spec elem]
    (when spec
      (let [opts {:renderer "canvas"
                  :mode "vega-lite"}]
        (-> (js/vegaEmbed elem spec (clj->js opts))
            (.then (fn [res]
                     #_(log res)
                     (. js/vegaTooltip (vegaLite (.-view res) spec))))
            (.catch (fn [err]
                      (log err)))))))

(defn ^{:category :data
        :R true}
  vega
  "displays chart defined in vega spec
   "
  [data-clj]
  ^:R
  [render-js {:f render-vega :data data-clj}])



