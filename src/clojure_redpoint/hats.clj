(ns clojure-redpoint.hats
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [clojure.repl :refer :all]))

(defn make-hat [plrs-map]
  (into [] (keys plrs-map)))

;(defn remove-puck-givee [ge-hat givee]
;  (into [] (filter #(not= % givee) ge-hat)))
;
;(defn remove-puck-giver [gr-hat giver]
;  (into [] (filter #(not= % giver) gr-hat)))
;
;(defn discard-puck-givee [discards givee]
;  (conj discards givee))
;
;(defn return-discards [ge-hat discards]
;  (into ge-hat discards))
;
;(defn empty-discards [_]
;  [])
