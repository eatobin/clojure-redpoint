(ns clojure-redpoint.player-test
  (:require [clojure.test :refer [deftest is]]
            [clojure-redpoint.domain :as dom]
            [clojure-redpoint.player :as plr]
            [clojure.spec.alpha :as s]
            [clojure-redpoint.gift-pair :as gp]))

(def plain-player {:player-name  "Paul McCartney",
                   :gift-history [{:giver :JohLen, :givee :GeoHar}]})

(def player (plr/map->Player {:player-name  "Paul McCartney",
                              :gift-history [(gp/->Gift-Pair :GeoHar :JohLen)]}))

(deftest player-update-gift-history-test
  (is (= (plr/map->Player {:player-name  "Paul McCartney",
                           :gift-history [(gp/->Gift-Pair :nope :yup)]})
         (plr/player-update-gift-history player [(gp/->Gift-Pair :nope :yup)]))))
(s/conform ::dom/player
           (plr/player-update-gift-history player [(gp/->Gift-Pair :nope :yup)]))

(deftest player-plain-upgrade-test
  (is (= player
         (plr/player-plain-upgrade plain-player))))
(s/conform ::dom/player
           (plr/player-plain-upgrade plain-player))
