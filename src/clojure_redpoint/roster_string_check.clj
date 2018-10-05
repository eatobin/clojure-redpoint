(ns clojure-redpoint.roster-string-check
  (:require [clojure.string :as str]))

(defn apply-or-error [f [val err]]
  (if (nil? err)
    (f val)
    [nil err]))

(defn scrub
  "Remove the spaces between CSVs and any final \n"
  [raw-string]
  (->
    raw-string
    (str/replace #", " ",")
    (str/trim-newline)))

(defn lines
  "Split string into lines"
  [scrubbed]
  (str/split-lines scrubbed))

(defn vec-remove
  "Remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn non-blank-string
  "Ensure string is not nil, empty or only spaces. Returns a scrubbed string"
  [raw-string]
  (if (str/blank? raw-string)
    [nil "The roster string was nil, empty or only spaces"]
    [(scrub raw-string) nil]))

(defn valid-length-string
  "A string of newlines >= 4?"
  [scrubbed]
  (if (<= 4 (count (filter #(= % \newline) scrubbed)))
    [scrubbed nil]
    [nil "Roster string is not long enough"]))

(defn roster-info-line-present
  "test"
  [scrubbed]
  (if (->
        scrubbed
        lines
        (get 0)
        non-blank-string
        (get 1)
        nil?)
    [scrubbed nil]
    [nil "The roster info line is blank"]))

(defn name-present
  "Return the raw-string if a name value is present"
  [scrubbed]
  (let [info-vector (->
                      scrubbed
                      lines
                      (get 0)
                      (str/split #","))]
    (if (->
          info-vector
          (get 0)
          (non-blank-string)
          (get 1)
          (nil?))
      [scrubbed nil]
      [nil "The name value is missing"])))

(defn year-present
  "Return the info-string if a year value is present"
  [scrubbed]
  (let [info-vector (->
                      scrubbed
                      lines
                      (get 0)
                      (str/split #","))]
    (if (= 2 (count info-vector))
      [scrubbed nil]
      [nil "The year value is missing"])))

(defn year-text-all-digits
  "Return the raw-info-string if the year text all digits"
  [scrubbed]
  (let [info-vector (->
                      scrubbed
                      lines
                      (get 0)
                      (str/split #","))]
    (if (->
          info-vector
          (get 1)
          (#(re-seq #"^[0-9]*$" %))
          (nil?)
          (not))
      [scrubbed nil]
      [nil "The year value is not all digits"])))

(defn year-in-range
  "Return the info-string if 1956 <= year <= 2056"
  [scrubbed]
  (let [info-vector (->
                      scrubbed
                      lines
                      (get 0)
                      (str/split #","))]
    (if (->
          info-vector
          (get 1)
          (Integer/parseInt)
          (#(<= 1956 % 2056)))
      [scrubbed nil]
      [nil "Not 1956 <= year <= 2056"])))

(defn make-player-vectors
  "Given a valid raw-string, return a vector of player vectors"
  [scrubbed]
  (vec-remove (->>
                (str/split-lines scrubbed)
                (map #(str/split % #","))
                (into []))
              0))

(defn remove-name
  "Given a player vector, return the vector without the player name"
  [player-vector]
  (vec-remove player-vector 1))

(defn make-only-symbols
  "Returns all player vectors void of names - symbols only"
  [player-vectors]
  (map remove-name player-vectors))

(defn all-six-chars?
  "All strings in the vector are 6 chars long"
  [player-symbols]
  (and (= 3 (count player-symbols))
       (= 3 (count (filter #(= (count %) 6) player-symbols)))))

(defn all-vectors-all-six?
  "All of the vectors only symbols"
  [player-vectors]
  (every? true? (map all-six-chars? player-vectors)))

(defn players-valid
  "Test"
  [scrubbed]
  (if (->
        (make-player-vectors scrubbed)
        (make-only-symbols)
        (all-vectors-all-six?))
    [scrubbed nil]
    [nil "The players sub-string is invalid"]))

(defn scrubbed-roster-string
  "Ensure that raw-string is scrubbed and fully valid"
  [raw-string]
  (let [result (non-blank-string raw-string)
        result (apply-or-error valid-length-string result)
        result (apply-or-error roster-info-line-present result)
        result (apply-or-error name-present result)
        result (apply-or-error year-present result)
        result (apply-or-error year-text-all-digits result)
        result (apply-or-error year-in-range result)
        result (apply-or-error players-valid result)]
    result))