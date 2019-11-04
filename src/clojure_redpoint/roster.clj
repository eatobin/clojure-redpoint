(ns clojure-redpoint.roster
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::roster-name string?)
(s/def ::roster-year int?)
(s/def :unq/roster (s/keys :req-un [::roster-name ::roster-year :unq/players]))


(defn get-roster-name
  "Given a roster return the roster name"
  [roster]
  (roster :roster-name))
(s/fdef get-roster-name
        :args (s/cat :roster :unq/roster)
        :ret ::roster-name)

;(defn get-roster-year
;  "Given a roster return the roster year"
;  [roster]
;  roster ::roster-year)
;(s/fdef get-roster-year
;        :args (s/cat :roster :unq/roster)
;        :ret ::roster-year)

(ostest/instrument)
