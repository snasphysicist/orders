(ns orders.json.json 
  (:require [clojure.data.json :as json])
  (:require [orders.dates.dates :as dates]))

(defn- serialisable-value
  [k v]
  (if (= (type v) java.time.LocalDate)
    (dates/date->string v)
    v))

(defn write-str
  "Wraps json/write-str with custom settings required by this service."
  [d]
  (json/write-str d :value-fn serialisable-value))

(defn read-str
  "Wraps json/read-str with custom settings required by this service."
  [d]
  (json/read-str d :key-fn keyword))
