(ns demo.notebook.vega-zoom)

^:R
['ui.vega/vega
 {:spec {:description
         "An interactive scatter plot example supporting pan and zoom."
         :autosize "none"
         :config
         {:axis
          {:domain false
           :tickSize 3
           :tickColor "#888"
           :labelFont "Monaco, Courier New"}}
         :axes
         [{:scale "xscale", :orient "top", :offset {:signal "xoffset"}}
          {:scale "yscale", :orient "right", :offset {:signal "yoffset"}}]
         :width 500
         :scales
         [{:name "xscale"
           :zero false
           :domain {:signal "xdom"}
           :range {:signal "xrange"}}
          {:name "yscale"
           :zero false
           :domain {:signal "ydom"}
           :range {:signal "yrange"}}]
         :padding {:top 10, :left 40, :bottom 20, :right 10}
         :marks
         [{:type "symbol"
           :from {:data "points"}
           :clip true
           :encode
           {:enter {:fillOpacity {:value 0.6}, :fill {:value "steelblue"}}
            :update
            {:x {:scale "xscale", :field "u"}
             :y {:scale "yscale", :field "v"}
             :size {:signal "size"}}
            :hover {:fill {:value "firebrick"}}
            :leave {:fill {:value "steelblue"}}
            :select {:size {:signal "size", :mult 5}}
            :release {:size {:signal "size"}}}}]
         :$schema "https://vega.github.io/schema/vega/v5.json"
         :signals
         [{:name "margin", :value 20}
          {:name "hover"
           :on
           [{:events "*:mouseover", :encode "hover"}
            {:events "*:mouseout", :encode "leave"}
            {:events "*:mousedown", :encode "select"}
            {:events "*:mouseup", :encode "release"}]}
          {:name "xoffset", :update "-(height + padding.bottom)"}
          {:name "yoffset", :update "-(width + padding.left)"}
          {:name "xrange", :update "[0, width]"}
          {:name "yrange", :update "[height, 0]"}
          {:name "down"
           :value nil
           :on
           [{:events "touchend", :update "null"}
            {:events "mousedown, touchstart", :update "xy()"}]}
          {:name "xcur"
           :value nil
           :on
           [{:events "mousedown, touchstart, touchend"
             :update "slice(xdom)"}]}
          {:name "ycur"
           :value nil
           :on
           [{:events "mousedown, touchstart, touchend"
             :update "slice(ydom)"}]}
          {:name "delta"
           :value [0 0]
           :on
           [{:events
             [{:source "window"
               :type "mousemove"
               :consume true
               :between
               [{:type "mousedown"} {:source "window", :type "mouseup"}]}
              {:type "touchmove"
               :consume true
               :filter "event.touches.length === 1"}]
             :update "down ? [down[0]-x(), y()-down[1]] : [0,0]"}]}
          {:name "anchor"
           :value [0 0]
           :on
           [{:events "wheel"
             :update "[invert('xscale', x()), invert('yscale', y())]"}
            {:events {:type "touchstart", :filter "event.touches.length===2"}
             :update "[(xdom[0] + xdom[1]) / 2, (ydom[0] + ydom[1]) / 2]"}]}
          {:name "zoom"
           :value 1
           :on
           [{:events "wheel!"
             :force true
             :update "pow(1.001, event.deltaY * pow(16, event.deltaMode))"}
            {:events {:signal "dist2"}, :force true, :update "dist1 / dist2"}]}
          {:name "dist1"
           :value 0
           :on
           [{:events {:type "touchstart", :filter "event.touches.length===2"}
             :update "pinchDistance(event)"}
            {:events {:signal "dist2"}, :update "dist2"}]}
          {:name "dist2"
           :value 0
           :on
           [{:events
             {:type "touchmove"
              :consume true
              :filter "event.touches.length===2"}
             :update "pinchDistance(event)"}]}
          {:name "xdom"
           :update "slice(xext)"
           :on
           [{:events {:signal "delta"}
             :update
             "[xcur[0] + span(xcur) * delta[0] / width, xcur[1] + span(xcur) * delta[0] / width]"}
            {:events {:signal "zoom"}
             :update
             "[anchor[0] + (xdom[0] - anchor[0]) * zoom, anchor[0] + (xdom[1] - anchor[0]) * zoom]"}]}
          {:name "ydom"
           :update "slice(yext)"
           :on
           [{:events {:signal "delta"}
             :update
             "[ycur[0] + span(ycur) * delta[1] / height, ycur[1] + span(ycur) * delta[1] / height]"}
            {:events {:signal "zoom"}
             :update
             "[anchor[1] + (ydom[0] - anchor[1]) * zoom, anchor[1] + (ydom[1] - anchor[1]) * zoom]"}]}
          {:name "size", :update "clamp(20 / span(xdom), 1, 1000)"}]
         :height 300
         :data
         [{:name "points"
           :url "/r/data/normal-2d.json"
           :transform
           [{:type "extent", :field "u", :signal "xext"}
            {:type "extent", :field "v", :signal "yext"}]}]}}]