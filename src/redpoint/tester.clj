(ns redpoint.tester
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(def bad-json "[ \"test\" :: 123 ]")
(def json-string-borrowers "[{\"max-books\":2, \"name\":\"Borrower2\"},{\"name\":\"Borrower1\",\"max-books\":1}]")
(def json-string-books "[{\"title\":\"Title1\",\"author\":\"Author1\",\"maybe-borrower\":{\"name\":\"Borrower1\",\"max-books\":1}},{\"title\":\"Title2\",\"author\":\"Author2\",\"maybe-borrower\":null}]")
(def file-path "resources-test/beatles.json")
(def bad-file-path "nope.json")
(def json-string-Roster "{\"rosterName\":\"The Beatles\",\"rosterYear\":2014,\"players\":{\"PauMcc\":{\"playerName\":\"Paul McCartney\",\"giftHistory\":[{\"givee\":\"GeoHar\",\"giver\":\"JohLen\"}]},\"GeoHar\":{\"playerName\":\"George Harrison\",\"giftHistory\":[{\"givee\":\"RinSta\",\"giver\":\"PauMcc\"}]},\"JohLen\":{\"playerName\":\"John Lennon\",\"giftHistory\":[{\"givee\":\"PauMcc\",\"giver\":\"RinSta\"}]},\"RinSta\":{\"playerName\":\"Ringo Starr\",\"giftHistory\":[{\"givee\":\"JohLen\",\"giver\":\"GeoHar\"}]}}}")

(try
  (slurp file-path)
  (catch Exception e
    (str (.getMessage e))))

(try
  (slurp bad-file-path)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str bad-json
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str json-string-borrowers
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))

(try
  (json/read-str json-string-books
                 :key-fn keyword)
  (catch Exception e
    (str (.getMessage e))))

(defn- my-key-reader
  [key]
  (cond
    (= key "playerName") :player-name
    (= key "giftHistory") :gift-history
    (= key "rosterName") :roster-name
    (= key "rosterYear") :roster-year
    :else (keyword key)))

(defn- my-value-reader
  [key value]
  (if (or (= key :givee)
          (= key :giver))
    (keyword value)
    value))

;(defn roster-json-string-to-Roster [json-string]
;  (json/read-str json-string
;                 :value-fn my-value-reader
;                 :key-fn my-key-reader))
;(s/fdef roster-json-string-to-Roster
;        :args (s/cat :json-string string?)
;        :ret :unq/roster)



(defn roster-json-string-to-Roster [json-string]
  (try
    (json/read-str json-string
                   :value-fn my-value-reader
                   :key-fn my-key-reader)
    (catch Exception e
      (str (.getMessage e)))))
(s/fdef roster-json-string-to-Roster
        :args (s/cat :json-string string?)
        :ret :unq/roster)

(roster-json-string-to-Roster json-string-Roster)
(roster-json-string-to-Roster json-string-borrowers)
(roster-json-string-to-Roster bad-json)

(ostest/instrument)
