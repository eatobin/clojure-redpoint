(ns clojure-redpoint.hats-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.hats :as hat]))

(def test-hat [:PauMcc :GeoHar :JohLen :RinSta])
(def players-map {:PauMcc {:name         "Paul McCartney",
                           :gift-history [{:giver :JohLen, :givee :GeoHar}]},
                  :GeoHar {:name         "George Harrison",
                           :gift-history [{:giver :PauMcc, :givee :RinSta}]},
                  :JohLen {:name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
                  :RinSta {:name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})

(deftest make-hats-test
  (is (= test-hat
         (hat/make-hat players-map))))

(deftest remove-puck-test
  (is (= [:PauMcc :GeoHar :JohLen]
         (hat/remove-puck test-hat :RinSta))))

(deftest remove-puck-empty-test
  (is (= []
         (hat/remove-puck [] :RinSta))))

(deftest discard-puck-givee-test
  (is (= [:PauMcc :JohLen]
         (hat/discard-puck-givee [:PauMcc] :JohLen))))

(deftest return-discards-test
  (is (= [:PauMcc :JohLen :GeoHar]
         (hat/return-discards [:PauMcc :JohLen] [:GeoHar]))))
