[:p/vegalite
 {:width 800
  :height 600
  :spec {:data {:url "/r/data/chicago-crimes-2018.arrow"
                ;:url "https://raw.githubusercontent.com/RandomFractals/ChicagoCrimes/master/data/2018/chicago-crimes-2018.arrow"
                :format {:type "arrow"}}
         :mark "bar"
         :encoding {:y {:field "PrimaryType"
                        :type "ordinal"
                        :sort {:encoding "x"}
                        :axis {:title "Crime Type"}}
                    :x {:aggregate "count"
                        :field "*"
                        :type "quantitative"
                        :axis {:title "# of Records"}}}}}]