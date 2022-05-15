(ns demo.notebook.vega-test)

^:R
['user/vega {:$schema "https://vega.github.io/schema/vega/v5.json"
             :width 400
             :height 247.2187886279357
             :padding {:top 10, :left 55, :bottom 40, :right 10}
             :data [{:name "50bbe3dd-798b-434d-b1b2-6095fee74a3d", :values [{:x 0, :y 7}
                                                                            {:x 1, :y 8}
                                                                            {:x 2, :y 7} {:x 3, :y 4} {:x 4, :y 6}]}]
             :marks [{:type "symbol"
                      :from {:data "50bbe3dd-798b-434d-b1b2-6095fee74a3d"}
                      :encode {:enter {:x {:scale "x", :field "x"}
                                       :y {:scale "y", :field "y"}
                                       :fill {:value "steelblue"}
                                       :fillOpacity {:value 1}}
                               :update {:shape "circle", :size {:value 70}, :stroke {:value "transparent"}}
                               :hover {:size {:value 210}, :stroke {:value "white"}}}}]
             :scales [{:name "x", :type "linear", :range "width", :zero false, :domain {:data "50bbe3dd-798b-434d-b1b2-6095fee74a3d", :field "x"}}
                      {:name "y", :type "linear", :range "height", :nice true, :zero false, :domain {:data "50bbe3dd-798b-434d-b1b2-6095fee74a3d", :field "y"}}]
             :axes [{:orient "bottom", :scale "x"} {:orient "left", :scale "y"}]}]

^:R
['user/vega {:$schema "https://vega.github.io/schema/vega/v5.json", :width 400, :height 247.2187886279357, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name "6348d171-8e7b-4a02-8b9a-bea64dbcbdce", :values [{:x 0, :y 7} {:x 1, :y 8} {:x 2, :y 7} {:x 3, :y 4} {:x 4, :y 6}]}], :marks [{:type "symbol", :from {:data "6348d171-8e7b-4a02-8b9a-bea64dbcbdce"}, :encode {:enter {:x {:scale "x", :field "x"}, :y {:scale "y", :field "y"}, :fill {:value "steelblue"}, :fillOpacity {:value 1}}, :update {:shape "circle", :size {:value 70}, :stroke {:value "transparent"}}, :hover {:size {:value 210}, :stroke {:value "white"}}}}], :scales [{:name "x", :type "linear", :range "width", :zero false, :domain {:data "6348d171-8e7b-4a02-8b9a-bea64dbcbdce", :field "x"}} {:name "y", :type "linear", :range "height", :nice true, :zero false, :domain {:data "6348d171-8e7b-4a02-8b9a-bea64dbcbdce", :field "y"}}], :axes [{:orient "bottom", :scale "x"} {:orient "left", :scale "y"}]}]

^:R
['user/vega {:$schema "https://vega.github.io/schema/vega/v5.json", :width 400, :height 247.2187886279357, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name "9612b327-d447-478a-84a6-27af98f319f0", :values [{:x 0, :y 7} {:x 1, :y 8} {:x 2, :y 7} {:x 3, :y 4} {:x 4, :y 6}]}], :marks [{:type "symbol", :from {:data "9612b327-d447-478a-84a6-27af98f319f0"}, :encode {:enter {:x {:scale "x", :field "x"}, :y {:scale "y", :field "y"}, :fill {:value "steelblue"}, :fillOpacity {:value 1}}, :update {:shape "circle", :size {:value 70}, :stroke {:value "transparent"}}, :hover {:size {:value 210}, :stroke {:value "white"}}}}], :scales [{:name "x", :type "linear", :range "width", :zero false, :domain {:data "9612b327-d447-478a-84a6-27af98f319f0", :field "x"}} {:name "y", :type "linear", :range "height", :nice true, :zero false, :domain {:data "9612b327-d447-478a-84a6-27af98f319f0", :field "y"}}], :axes [{:orient "bottom", :scale "x"} {:orient "left", :scale "y"}]}]
