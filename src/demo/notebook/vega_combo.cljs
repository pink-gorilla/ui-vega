(ns demo.notebook.vega-combo)

(def demo-charts
  [{:label "bar-chart"
    :id "/r/vega/bar-chart.vg.json"}
   {:label "population-pyramid" :id "/r/vega/population-pyramid.vg.json"}
   {:label "tree-layout" :id "/r/vega/tree-layout.vg.json"}
   {:label "tree-map" :id "/r/vega/treemap.vg.json"}
   {:label "force directed layout" :id "/r/vega/force-directed-layout.vg.json"}
   {:label "stock index" :id "/r/vega/stock-index-chart.vg.json"}
   {:label "overview-details" :id "/r/vega/overview-plus-detail.vg.json"}
   {:label "scatterplot interaction" :id "/r/vega/brushing-scatter-plots.vg.json"}
   {:label "unemployment map" :id "/r/vega/county-unemployment.vg.json"}
   {:label "box plot" :id "/r/vega/box-plot.vg.json"}
   {:label "contour" :id "/r/vega/contour-plot.vg.json"}])

[:div
 [:h1 "select the sample vega plot you want to see"]
 [:p/select {:nav? false
             :items demo-charts
             :display :label} state [:vega]]
 [:p "you selected: " (:vega @state)]
 (when-let [spec (get-in @state [:vega :id])]
            ;[:div "spec:" (pr-str (:spec spec))]
   ['user/vega {:spec spec}])]
