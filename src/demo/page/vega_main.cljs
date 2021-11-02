

(defn vegademo-page []
  [:div
   [:h1 "vega demo page"]
   [link-dispatch [:bidi/goto :vegademo/histogram] "histogram"]
   [link-dispatch [:bidi/goto :devtools] "dev-tools"]])

(add-page vegademo-page :vegademo/main)
