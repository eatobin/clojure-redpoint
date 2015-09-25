(ns clojure-redpoint.roster-test
  (:require [clojure.test :refer :all]
            [clojure-redpoint.roster :refer :all]))

(defn setup []
  (spit "roster-test.txt" "Blackhawks, 1956
TroBro, Troy Brouwer, DavBol, JoeQue
JoeQue, Joel Quenneville, TroBro, AndLad
AdaBur, Adam Burish, DunKei, JonToe\n")
  (make-roster "roster-test.txt"))

(defn teardown [])

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each each-fixture)

(deftest make-roster-test
  (is (= 5
         (count (deref roster))))
  (is (= [:team-name "Blackhawks"]
         (first (deref roster)))))

(deftest get-player-name-test
  (is (= "Adam Burish"
         (get-player-name :AdaBur))))

(deftest add-history-test
  (is (= {:team-name  "Blackhawks",
          :first-year 1956,
          :TroBro     {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol} {:givee :test1, :giver :test2}]},
          :JoeQue     {:name "Joel Quenneville", :gift-history [{:giver :AndLad, :givee :TroBro}]},
          :AdaBur     {:name "Adam Burish", :gift-history [{:giver :JonToe, :givee :DunKei}]}}
         (add-history :TroBro 1 :test1 :test2))))

(deftest get-givee-code-test
  (is (= nil
         (get-givee-code :TroBroX 0)))
  (is (= nil
         (get-givee-code :TroBro 9)))
  (is (= :DavBol
         (get-givee-code :TroBro 0))))

(deftest set-givee-code-test
  (is (= {:team-name  "Blackhawks",
          :first-year 1956,
          :TroBro     {:name "Troy Brouwer", :gift-history [{:giver :JoeQue, :givee :DavBol}]},
          :JoeQue     {:name "Joel Quenneville", :gift-history [{:giver :AndLad, :givee :TroBro}]},
          :AdaBur     {:name "Adam Burish", :gift-history [{:giver :JonToe, :givee :test1}]}}
         (set-givee-code :AdaBur 0 :test1)))
  (is (= nil
         (set-givee-code :AdaBurX 0 :test1)))
  (is (= nil
         (set-givee-code :AdaBur 1 :test1))))
