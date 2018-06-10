(ns clojure-redpoint.roster
  (:require [clojure.string :as cs]
            [clojure-csv.core :as csv]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]))

(s/def ::roster-seq (s/coll-of vector?))
(s/def ::plrs-list (s/coll-of vector?))
(s/def ::givee keyword?)
(s/def ::giver keyword?)
(s/def :unq/gift-pair (s/keys :req-un [::givee ::giver]))
(s/def ::name string?)
(s/def :unq/gift-history (s/coll-of :unq/gift-pair))
(s/def :unq/player (s/keys :req-un [::name :unq/gift-history]))
(s/def ::plr-map-vec (s/coll-of string? :kind vector? :count 4 :distinct false))

(defn- make-roster-seq
  "Returns a lazy roster-seq"
  [roster-string]
  (let [de-spaced (cs/replace roster-string #", " ",")]
    (csv/parse-csv de-spaced)))
(s/fdef make-roster-seq
        :args (s/cat :roster-string string?)
        :ret ::roster-seq)

(defn- extract-roster-info-vector [roster-string]
  (first (make-roster-seq roster-string)))
(s/fdef extract-roster-info-vector
        :args (s/cat :roster-string string?)
        :ret (s/or :found vector?
                   :not-found nil?))

(defn- extract-players-list [roster-string]
  (into () (rest (make-roster-seq roster-string))))
(s/fdef extract-players-list
        :args (s/cat :roster-string string?)
        :ret ::plrs-list)

(defn- make-gift-pair [givee giver]
  (hash-map
    :givee (keyword givee)
    :giver (keyword giver)))
(s/fdef make-gift-pair
        :args (s/cat :givee string? :giver string?)
        :ret :unq/gift-pair)

(defn- make-player [p-name g-hist]
  (hash-map
    :name p-name
    :gift-history g-hist))
(s/fdef make-player
        :args (s/cat :p-name ::name :g-hist :unq/gift-history)
        :ret :unq/player)

(defn- make-player-map [[s n ge gr]]
  (let [gp (make-gift-pair ge gr)
        plr (make-player n (vector gp))]
    (hash-map
      (keyword s) plr)))
(s/fdef make-player-map
        :args (s/cat :arg1 ::plr-map-vec)
        :ret map?)

(defn make-players-map [roster-string]
  (let [pl (extract-players-list roster-string)]
    (into {} (map make-player-map pl))))
(s/fdef make-players-map
        :args (s/cat :roster-string string?)
        :ret map?)

(defn get-player-in-roster [plrs-map plr-sym]
  (get plrs-map plr-sym))
(s/fdef get-player-in-roster
        :args (s/cat :plrs-map map? :plr-sym keyword?)
        :ret (s/or :unq/player nil?))

;(defn get-roster-name [roster-list]
;  (let [line (extract-roster-info-vector roster-list)]
;    (first line)))
;
;(defn get-roster-year [roster-list]
;  (let [line (extract-roster-info-vector roster-list)]
;    (read-string (last line))))
;
;(defn get-player-name-in-roster [plrs-map plr-sym]
;  (let [plr (get-player-in-roster plrs-map plr-sym)]
;    (get plr :name)))
;
;(defn get-givee-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-givee-in-gift-pair gp)))
;
;(defn get-giver-in-roster [plrs-map plr-sym g-year]
;  (let [gp (get-gift-pair-in-roster plrs-map plr-sym g-year)]
;    (get-giver-in-gift-pair gp)))
;
;(defn set-givee-in-roster [plrs-map plr-sym g-year ge]
;  (if (check-give plrs-map plr-sym g-year ge)
;    (let [gr (get-giver-in-roster plrs-map plr-sym g-year)
;          gp (make-gift-pair ge gr)]
;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;    plrs-map))
;
;(defn set-giver-in-roster [plrs-map plr-sym g-year gr]
;  (if (check-give plrs-map plr-sym g-year gr)
;    (let [ge (get-givee-in-roster plrs-map plr-sym g-year)
;          gp (make-gift-pair ge gr)]
;      (set-gift-pair-in-roster plrs-map plr-sym g-year gp))
;    plrs-map))

(st/instrument)

(def rs "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")
(s/conform vector?
           (extract-roster-info-vector rs))
(s/conform nil?
           (extract-roster-info-vector ""))
(s/conform ::plrs-list
           (extract-players-list rs))
(s/conform ::plrs-list
           (extract-players-list ""))
(def x (make-gift-pair "joe" "bob"))
(def y (make-gift-pair "joey" "bobby"))
(def h [x y])
(s/conform :unq/gift-history h)
(s/conform :unq/player
           (make-player "eric" h))
(s/conform map? (make-player-map ["s" "n" "ge" "gr"]))
