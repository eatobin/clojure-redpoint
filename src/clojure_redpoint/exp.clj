(require '[clojure.string :as str])

(def roster-string "The Beatles, 2014\nRinSta, Ringo Starr, JohLen, GeoHar\nJohLen, John Lennon, PauMcc, RinSta\nGeoHar, George Harrison, RinSta, PauMcc\nPauMcc, Paul McCartney, GeoHar, JohLen\n")

(defn de-space [rs] (str/replace rs #", " ","))

(def lines (str/split-lines (de-space roster-string)))

(get lines 0)

(def info (get lines 0))

(def info-bad "The Beatles,")

(str/split info #",")

(str/split info-bad #",")

(def info-bad2 ",2014")

(str/split info-bad2 #",")

(str/split "" #",")

(str/split " " #",")

(def freqs (frequencies roster-string))

(count (filter #(= % \newline) roster-string))

(str/blank? roster-string)

(= 2 (count (str/split info #",")))

(get info 1)

(def year (read-string (get (str/split info #",") 1)))

(#(<= 1956 % 2056) year)
