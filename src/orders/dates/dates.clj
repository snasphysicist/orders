(ns orders.dates.dates
  (:require [orders.errors.errors :as errors]))

(defn parse-date 
  "Parse an ISO-8601-ish (2023-03-19) date string."
  [d]
  (try
    {:result :ok
     :value (java.time.LocalDate/parse 
              d 
              (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd"))}
    (catch java.time.format.DateTimeParseException e
      {:result :error 
       :type errors/bad-input 
       :message (str e)})))

(defn date->string
  "Write a java.time.LocalDate to an ISO-8601-ish (2023-04-19) string."
  [d]
  (.format d 
    (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd")))
