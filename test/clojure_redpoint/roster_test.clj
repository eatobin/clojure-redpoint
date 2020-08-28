(ns clojure-redpoint.roster-test
  (:require [clojure-redpoint.player :as plr]
            [clojure.test :refer [deftest is]]
            [clojure-redpoint.roster :as ros]
            [clojure.spec.alpha :as s]))

(def roster {:roster-name "The Beatles",
             :roster-year 2014,
             :players     {:PauMcc (plr/map->Player {:player-name  "Paul McCartney",
                                                     :gift-history [{:giver :JohLen, :givee :GeoHar}]}),
                           :GeoHar (plr/map->Player {:player-name  "George Harrison",
                                                     :gift-history [{:giver :PauMcc, :givee :RinSta}]}),
                           :JohLen (plr/map->Player {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}),
                           :RinSta (plr/map->Player {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]})}})

(s/conform ::ros/roster
           roster)

(deftest roster-name-test
  (is (= "The Beatles"
         (:roster-name roster))))

(deftest roster-year-test
  (is (= 2014
         (:roster-year roster))))
