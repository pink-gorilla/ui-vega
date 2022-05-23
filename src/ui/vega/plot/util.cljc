(ns ui.vega.plot.util
  #?(:clj
     (:require
      [clojure.java.io] ; this brings java.util to scope; TODO: check if this can be done in simpler way
      )))

#?(:clj
   (defn gen-uuid [] (str (java.util.UUID/randomUUID))))

#?(:cljs
   (defn gen-uuid [] (str (cljs.core/random-uuid))))

(defn count-in-range
  [data min max]
  (count (filter #(and (< % max) (>= % min)) data)))

(defn bin-counts
  [data min max bin-width]
  (let [bin-starts (range min max bin-width)]
    (map #(count-in-range data % (+ % bin-width)) bin-starts)))

(defn random-data [& names]
  (apply concat (for [n names]
                  (map-indexed (fn [i x] {:x i :y x :col n}) (take 20 (repeatedly #(rand-int 100)))))))