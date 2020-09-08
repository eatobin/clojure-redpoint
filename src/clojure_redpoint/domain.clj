(ns clojure-redpoint.domain
  (:require [clojure.spec.alpha :as s]))

(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def ::gift-pair (s/keys :req-un [::givee ::giver]))

(s/def ::gift-history (s/coll-of ::gift-pair :kind vector?))
