(ns pinkgorilla.vega.plot.swings)

(def swingchart-spec
  {;:$schema "http://localhost:8000/r/vega-lite/build/vega-lite-schema.json"  ;https://vega.github.io/schema/vega-lite/v5.json",
 ;:data {:url "/r/data/swings.json"}
   :data {:name "swings"}
   :layer [{;:mark "rect" ;
            :mark {:type "rect"
                   :tooltip {:content "data"}
                 ;:height 100 
                   :width 10}
            :encoding {"x"   {:field "idx"  :type "quantitative"}
                       "width" 1 ; {:band 1 :type "quantitative"}
                     ;"x2"  {:field "idx2"  :type "quantitative"}
                       "y"   {:field "Low"  :type "quantitative"}
                       "y2"  {:field "High" :type "quantitative"}
                            ;"color" {:value  "orange"}
                       "color" {:field "dir"
                                :type "nominal"
                                :scale {:domain ["up" "down"]
                                        :range ["green" "#c7c7c7"]}

                                :legend nil}}}
           {:mark {:type "point",
                   :height 10
                   :width 20}
            :encoding {"x"   {:field "idx", :type "quantitative"}
                       "y"   {:field "High", :type "quantitative"}
                       "color" {:value  "red"}}}]})

(defn swing-chart [{:keys [data] :as opts}]
  (let [opts (assoc opts :spec swingchart-spec)]
    ^:R [:p/vegalite opts]))