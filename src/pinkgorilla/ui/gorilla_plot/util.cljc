(ns pinkgorilla.ui.gorilla-plot.util)

#?(:clj
   (defn uuid [] (str (java.util.UUID/randomUUID))))

#?(:cljs
   (defn uuid [] (str (cljs.core/random-uuid))))

(defn count-in-range
  [data min max]
  (count (filter #(and (< % max) (>= % min)) data)))

(defn bin-counts
  [data min max bin-width]
  (let [bin-starts (range min max bin-width)]
    (map #(count-in-range data % (+ % bin-width)) bin-starts)))
