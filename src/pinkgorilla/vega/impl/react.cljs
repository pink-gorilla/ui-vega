(ns pinkgorilla.vega.impl.react
  (:require
   ["react-vega" :refer [VegaLite Vega]]
   ["vega-tooltip" :refer [Handler]]
   [pinkie.box :refer [apply-style]]
   [pinkgorilla.vega :refer [vega-opts]]))

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