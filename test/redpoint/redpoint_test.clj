(ns redpoint.redpoint-test
  (:require [clojure.test :refer [deftest is]]
            [redpoint.domain :as dom]
            [redpoint.redpoint :as core]
            [redpoint.players :as plrs]
            [redpoint.hats :as hat]
            [clojure.spec.alpha :as s]))

(def test-hat #{:PauMcc :GeoHar :JohLen :RinSta})
(def players {:PauMcc {:player-name  "Paul McCartney",
                       :gift-history [{:giver :JohLen, :givee :GeoHar}]},
              :GeoHar {:player-name  "George Harrison",
                       :gift-history [{:giver :PauMcc, :givee :RinSta}]},
              :JohLen {:player-name "John Lennon", :gift-history [{:giver :RinSta, :givee :PauMcc}]},
              :RinSta {:player-name "Ringo Starr", :gift-history [{:giver :GeoHar, :givee :JohLen}]}})
(def file-path "resources-test/beatles.json")
(def bad-file-path "nope.json")
(def bad-json-file "resources-test/bad-json.json")

(deftest roster-or-quit-success-test
  (core/roster-or-quit file-path)
  (is (= "The Beatles"
         (deref core/a-roster-name)))
  (is (= 2014
         (deref core/a-roster-year)))
  (is (= players
         (deref core/a-players))))

(deftest roster-or-quit-bad-path-test
  (is (nil? (core/roster-or-quit bad-file-path))))

(deftest roster-or-quit-bad-json-test
  (is (nil? (core/roster-or-quit bad-json-file))))

(s/conform ::dom/roster-name
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-roster-name)))
(s/conform ::dom/roster-year
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-roster-year)))
(s/conform :unq/players
           (do
             (core/roster-or-quit "resources-test/beatles.json")
             (deref core/a-players)))

(deftest draw-puck-test
  (is (true?
        (nil? (core/draw-puck #{}))))
  (is (true?
        (some? (core/draw-puck test-hat)))))
(s/conform ::dom/givee
           (core/draw-puck test-hat))
(s/conform nil?
           (core/draw-puck #{}))

(deftest start-new-year-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (is (= 1
         (deref core/a-g-year)))
  (is (some?
        (deref core/a-giver)))
  (is (some?
        (deref core/a-givee)))
  (is (= {:player-name  "Ringo Starr",
          :gift-history [{:givee :JohLen, :giver :GeoHar}
                         {:givee :RinSta, :giver :RinSta}]}
         (get-in (deref core/a-players) [:RinSta])))
  (is (empty? (deref core/a-discards))))

(deftest select-new-giver-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (swap! core/a-discards hat/discard-givee :GeoHar)
  (is (= 1
         (count (deref core/a-discards))))
  (core/select-new-giver)
  (is (= 3
         (count (deref core/a-gr-hat))))
  (is (= 0
         (count (deref core/a-discards)))))

(deftest givee-is-success-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (let [temp-ge (deref core/a-givee)]
    (core/givee-is-success)
    (is (= temp-ge
           (plrs/players-get-givee (deref core/a-players) (deref core/a-giver) (deref core/a-g-year))))
    (is (= (deref core/a-giver)
           (plrs/players-get-giver (deref core/a-players) temp-ge (deref core/a-g-year))))
    (is (= nil
           (some #{temp-ge} (deref core/a-ge-hat))))))

(deftest givee-is-failure-test
  (reset! core/a-g-year 0)
  (reset! core/a-giver nil)
  (reset! core/a-givee nil)
  (core/roster-or-quit "resources-test/beatles.json")
  (core/start-new-year)
  (let [temp-ge (deref core/a-givee)]
    (core/givee-is-failure)
    (is (= temp-ge
           (some #{temp-ge} (deref core/a-discards))))
    (is (= nil
           (some #{temp-ge} (deref core/a-ge-hat))))))

(deftest errors?-test
  (reset! core/a-g-year 0)
  (reset! core/a-players {:RinSta {:player-name "Ringo Starr", :gift-history [{:givee :JohLen, :giver :GeoHar}]},
                          :JohLen {:player-name "John Lennon", :gift-history [{:givee :PauMcc, :giver :RinSta}]},
                          :GeoHar {:player-name "George Harrison", :gift-history [{:givee :GeoHar, :giver :whoops}]},
                          :PauMcc {:player-name "Paul McCartney", :gift-history [{:givee :yikes, :giver :PauMcc}]}})
  (is (= (seq [:GeoHar :PauMcc])
         (core/errors?))))
