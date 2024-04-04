(ns ui.vega.impl.react
  (:require
   [reagent.core :as r]
   ["react-vega" :refer [#_VegaLite Vega]]
   ["vega-tooltip" :refer [Handler]]

   ; arrow format
   ["vega" :refer [formats]]
   ;["apache-arrow" :as aa]
   ["./apache-arrow.js" :as aa]
   ;["vega-loader-arrow" :as arrow]
   ;["flatbuffers" :as fb]
   ["./vega-loader-arrow.js" :as arrow]

   [pinkie.ui.core :refer [apply-box-style]]
   [ui.vega :refer [vega-opts]]))

;(println "fb: " fb)

(def arrow-needs-load (r/atom true))
(defn ensure-arrow! []
  (when @arrow-needs-load
    (println "loading arrow...")
    (reset! arrow-needs-load false)
; register arrow reader under type 'arrow'
    (formats "arrow" arrow)))

(def tt-handler
  (let [h (Handler.)]
    ;(println "tt handler: " h)
    h))

(def tt-call
  (let [c (. tt-handler -call)]
    ;(println "tt call: " c)
    c))
; tooltip= {new Handler () .call}

; const signalListeners = { hover: handleHover };
; signalListeners= {signalListeners}

(defn handle-hover [& args]
  (println "hover: " args))

(defn handle-tooltip [t & args]
  (println "tooltip: " (js->clj args)))

(defn handle-parse-error [& args]
  (println "vega spec parse error: " args))

(def signal-listeners
  {;:hover handle-hover
   :tooltip  handle-tooltip
   ;:on-parse-error handle-parse-error
   })

(defn vega [opts]
  (ensure-arrow!)
  (let [opts (if (:spec opts) opts {:spec opts}) ; old syntax compatibility
        spec (:spec opts)
        user-opts (select-keys spec [:width :height :overflow])
        spec (if (map? spec) ; spec could be a map or astring (url)
               (-> (apply-style spec) ; box inject
                   (assoc :usermeta {:embedOptions (merge vega-opts user-opts)}))
               spec)]
    [:> Vega (merge opts
                    {; All signals defined in the spec can be listened to via signalListeners
                     :signalListeners signal-listeners ; (clj->js signal-listeners)
                     :tooltip tt-call
                     :actions {:export true :source true :compiled true :editor false}
                     :onParseError handle-parse-error
                     :spec spec})]))

(defn vegalite [opts]
  [vega (merge {:mode "vega-lite"} opts)])

; themes:
; https://observablehq.com/@vega/vega-themes-demo