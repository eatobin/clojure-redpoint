(ns clojure-redpoint.players
  (:require [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::player-key keyword?)
(s/def :unq/players (s/map-of ::player-key :unq/player))

(defn players-update-player
  [players plr-key player]
  (assoc players plr-key player))
(s/fdef players-update-player
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :player :unq/player)
        :ret :unq/players)

(defn players-get-player-name
  [players plr-key]
  (:player-name (get players plr-key)))
(s/fdef players-get-player-name
        :args (s/cat :players :unq/players
                     :plr-key ::player-key)
        :ret (s/or :found ::player-name
                   :not-found nil?))

(defn add-year
  "Add a year for each player in roster"
  [players]
  (into {} (for [[plr-key player] players]
             (let [{:keys [player-name gift-history]} player]
               [plr-key {:player-name  player-name,
                         :gift-history (conj gift-history {:givee plr-key, :giver plr-key})}]))))
(s/fdef add-year
        :args (s/cat :players :unq/players)
        :ret :unq/players)

(defn get-givee
  [players plr-key g-year]
  (get-in players [plr-key :gift-history g-year :givee]))
(s/fdef get-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year)
        :ret ::givee)

(defn get-giver
  [players plr-key g-year]
  (get-in players [plr-key :gift-history g-year :giver]))
(s/fdef get-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year)
        :ret ::giver)

(defn set-givee
  [players plr-key g-year givee]
  (assoc-in players [plr-key :gift-history g-year :givee] givee))
(s/fdef set-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year
                     :givee ::givee)
        :ret :unq/players)

(defn set-giver
  [players plr-key g-year giver]
  (assoc-in players [plr-key :gift-history g-year :giver] giver))
(s/fdef set-givee
        :args (s/cat :players :unq/players
                     :plr-key ::player-key
                     :g-year ::gift-year
                     :giver ::giver)
        :ret :unq/players)

(ostest/instrument)
