(ns clojure-redpoint.main
  (:require [clojure.string :as cs]
            [clojure-redpoint.roster :refer :all]
            [clojure-redpoint.hats :refer :all]
            [clojure-redpoint.rules :refer :all])
  (:gen-class))

;(def year (atom 0))
;(def giver (atom :none))
;(def givee (atom :none))
;
;(defn initialize-state []
;  (reset! year 0)
;  (reset! giver :none)
;  (reset! givee :none)
;  (make-roster "blackhawks2010.txt"))
;
;(defn start-new-year []
;  (swap! year inc)
;  (add-new-year)
;  (make-hats roster)
;  (reset! giver (draw-puck-giver))
;  (reset! givee (draw-puck-givee)))
;
;(defn select-new-giver []
;  (remove-puck-giver (deref giver))
;  (return-discards)
;  (reset! giver (draw-puck-giver))
;  (reset! givee (draw-puck-givee)))
;
;(defn givee-is-success []
;  (set-givee-code (deref giver)
;                  (deref year) (deref givee))
;  (set-giver-code (deref givee)
;                  (deref year) (deref giver))
;  (remove-puck-givee (deref givee))
;  (reset! givee nil))
;
;(defn givee-is-failure []
;  (discard-puck (deref givee))
;  (reset! givee (draw-puck-givee)))
;
;(defn print-and-ask []
;  (println (print-string-giving-roster (deref year)))
;  (println "Continue? ('q' to quit): ")
;  (read-line))
;
(defn -main []
  ;  (initialize-state)
  ;  (while (not= (cs/lower-case (print-and-ask)) "q")
  ;    (start-new-year)
  ;    (while (some? (deref giver))
  ;      (while (some? (deref givee))
  ;        (if (and
  ;              (givee-not-self? (deref giver) (deref givee))
  ;              (givee-not-recip? (deref giver) (deref givee) (deref year))
  ;              (givee-not-repeat? (deref giver) (deref givee) (deref year)))
  ;          (givee-is-success)
  ;          (givee-is-failure)))
  ;      (select-new-giver))
  ;    (println))
  ;  (println)
  ;  (println "This was fun!")
  ;  (println "Talk about a position with Redpoint?")
  ;  (println "Please call: Eric Tobin 773-325-1516")
  (println "Thanks! Bye...")
  (println))


;(defn print-string-giving-roster [gift-year]
;  (let [no-givee (atom [])
;        no-giver (atom [])
;        roster-string (atom [])]
;    (swap! roster-string conj team-name " - Year " (+ first-year gift-year) " Gifts:\n\n")
;    (doseq [p (keys (into (sorted-map) (deref roster)))]
;      (let [player-name (get-player-name p)
;            givee-code (get-givee-code p gift-year)
;            giver-code (get-giver-code p gift-year)]
;        (if (= givee-code :none)
;          (swap! no-givee conj p)
;          (swap! roster-string conj player-name " is buying for " (get-player-name givee-code) "\n"))
;        (if (= giver-code :none)
;          (swap! no-giver conj p))))
;    (if-not (and (empty? (deref no-givee))
;                 (empty? (deref no-giver)))
;      (do
;        (swap! roster-string conj "\nThere is a logic error in this year's pairings.\nDo you see it?\nIf not... call me and I'll explain!\n\n")
;        (doseq [p (deref no-givee)]
;          (swap! roster-string conj (get-player-name p) " is buying for no one.\n"))
;        (doseq [p (deref no-giver)]
;          (swap! roster-string conj (get-player-name p) " is receiving from no one.\n"))))
;    (apply str (deref roster-string))))
