(ns clojure-redpoint.player
  (:require [clojure-redpoint.gift-history :as gh]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-name string?)
(s/def :unq/player (s/keys :req-un [::player-name :unq/gift-history]))

(defrecord Player [player-name gift-history])
(s/fdef ->Player
        :args (s/cat :player-name ::player-name
                     :gift-history :unq/gift-history)
        :ret :unq/player)

(defn get-player-name
  "Return a player-name given a player"
  [player]
  (player :player-name))
(s/fdef get-player-name
        :args (s/cat :player :unq/player)
        :ret ::player-name)

(defn get-gift-history
  "Return a gift-history given a player"
  [player]
  (player :gift-history))
(s/fdef get-gift-history
        :args (s/cat :player :unq/player)
        :ret :unq/gift-history)

(defn set-gift-history
  "Sets a gift history into the provided player"
  [player g-hist]
  (assoc player :gift-history g-hist))
(s/fdef set-gift-history
        :args (s/cat :player :unq/player
                     :g-hist :unq/gift-history)
        :ret :unq/player)

(defn add-year-player
  "Adds a new placeholder year to the end of a player's gift history"
  [player plr-key]
  (->
    player
    (get-gift-history)
    (gh/add-year plr-key)
    (->>
      (set-gift-history player))))
(s/fdef add-year-player
        :args (s/cat :player :unq/player
                     :plr-key ::gh/player-key)
        :ret :unq/player)

(ostest/instrument)
