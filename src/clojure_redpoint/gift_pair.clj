(ns clojure-redpoint.gift-pair
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))

(defn get-givee
  "Returns a givee given a gift pair"
  [g-pair]
  (g-pair :givee))
(s/fdef get-givee
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::givee)

(defn get-giver
  "Returns a giver given a gift pair"
  [g-pair]
  (g-pair :giver))
(s/fdef get-giver
        :args (s/cat :g-pair :unq/gift-pair)
        :ret ::giver)

(defn set-givee
  "Returns a gift pair with updated givee"
  [g-pair n-givee]
  (assoc g-pair :givee n-givee))
(s/fdef set-givee
        :args (s/cat :g-pair :unq/gift-pair
                     :n-givee ::givee)
        :ret :unq/gift-pair)

(defn set-giver
  "Returns a gift pair with updated giver"
  [g-pair n-giver]
  (assoc g-pair :giver n-giver))
(s/fdef set-giver
        :args (s/cat :g-pair :unq/gift-pair
                     :n-giver ::giver)
        :ret :unq/gift-pair)

(ostest/instrument)
