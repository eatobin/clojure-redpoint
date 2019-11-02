(ns clojure-redpoint.players-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.player :as plr]
            [clojure-redpoint.players :as plrs]
            [clojure.spec.alpha :as s]))

(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(def new-bee-players {:PauMcc {:player-name  "Paul McCartney",
                               :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                      :GeoHar {:player-name  "George Harrison",
                               :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                      :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                      :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}})

(def extended-players {:PauMcc {:player-name  "Paul McCartney",
                                :gift-history [{:giver :JohLen, :givee :GeoHar} {:givee :PauMcc, :giver :PauMcc}]},
                       :GeoHar {:player-name  "George Harrison",
                                :gift-history [{:giver :PauMcc, :givee :RinSta} {:givee :GeoHar, :giver :GeoHar}]},
                       :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc} {:givee :JohLen, :giver :JohLen}]},
                       :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen} {:givee :RinSta, :giver :RinSta}]}})

(s/conform :unq/players
           players)

(deftest get-player-pass-test
  (is (= {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]}
         (plrs/get-player players :JohLen))))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (plrs/get-player players :JohLen))

(deftest get-player-fail-test
  (is (= nil
         (plrs/get-player players :JohLenX))))
(s/conform (s/or :found :unq/player
                 :not-found nil?)
           (plrs/get-player players :JohLenX))

(deftest set-player-test
  (is (= new-bee-players
         (plrs/set-player
           players
           :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}))))
(s/conform :unq/players
           (plrs/set-player
             players
             :RinSta {:player-name "New Bee", :gift-history [{:giver :NewBee, :givee :NewBee}]}))

(deftest add-year-players-test
  (is (= extended-players
         (plrs/add-year-players players))))
(s/conform :unq/players
           (plrs/add-year-players players))

(deftest get-player-name-players-test
  (is (= "George Harrison"
         (plrs/get-player-name-players players :GeoHar))))
(s/conform ::plr/player-name
           (plrs/get-player-name-players players :GeoHar))
