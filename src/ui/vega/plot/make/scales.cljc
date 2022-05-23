(ns ui.vega.plot.make.scales)

(defn- domain-helper
  [data-key axis-plot-range axis]
  (if (= axis-plot-range :all)
    {:data data-key, :field (str axis)}
    axis-plot-range))

(defn default-list-plot-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "linear"
             :range  "width"
             :zero   false
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :type   "linear"
             :range  "height"
             :nice   true
             :zero   false
             :domain (domain-helper data-key (second plot-range) "y")}]})

(defn timeseries-list-plot-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "time"
             :range  "width"
             :zero   false
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :type   "linear"
             :range  "height"
             :nice   true
             :zero   false
             :domain (domain-helper data-key (second plot-range) "y")}]})

(defn default-bar-chart-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "band" ; "ordinal"
             :range  "width"
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :range  "height"
             :nice   true
             :domain (domain-helper data-key (second plot-range) "y")}]})

