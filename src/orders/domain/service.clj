(ns orders.domain.service
  (:require [orders.repository.postgresql :as repository])
  (:require [orders.domain.validation :as validation])
  (:require [orders.dates.dates :as dates])
  (:require [orders.errors.errors :as errors])
  (:require [orders.result.result :as result]))

(defn initialise 
  "Run all initialisation/setup tasks."
  [] 
  (repository/initialise))

(defn create-order 
  "Create a new order with the given details."
  [o]
  (let [r (validation/contains-keys? o [:customer :date])]
    (if (result/ok? r)
      (let [r (validation/has-types? o {:customer java.lang.String :date java.lang.String})]
        (if (result/ok? r)
          (let [d (dates/parse-date (:date o))]
            (if (result/ok? r)
              {:result :ok
               :value (repository/create-order 
                        (merge o {:date (:value d)}))}
              d))
          r))
      r)))

(defn- invalid-integer-error
  "Creates an error representing an invalid input for an integer."
  [s]
  {:result :error 
   :type errors/bad-input
   :message (str "'" s "' is not a valid integer")})

(defn- string->integer
  "Attempts to convert a string to an integer."
  [s]
  (try 
    {:result :ok :value (Integer. s)} 
    (catch java.lang.NumberFormatException e
      (invalid-integer-error s))
    (catch java.lang.IllegalArgumentException e
      (invalid-integer-error s))))

(defn read-order 
  "Read an order with the given id."
  [sid]
  (let [id (string->integer sid)]
    (if (result/ok? id)
      (repository/read-order (:value id))
      id)))

(defn delete-order
  "Delete an order with the given id."
  [sid]
  (let [id (string->integer sid)]
    (if (result/ok? id)
      (repository/delete-order (:value id))
      id)))

(defn add-line-item
  "Add the given line item to the order with the given id."
  [li sid]
  (let [id (string->integer sid)]
    (if (result/ok? id)
      (let [r (validation/contains-keys? li [:product :quantity])]
        (if (result/ok? r)
          (let [r (validation/has-types? li {:product java.lang.String :quantity java.lang.Long})]
            (if (result/ok? r)
              (let [o (repository/read-order (:value id))]
                (if (result/ok? o)
                  {:result :ok
                   :value (repository/add-line-item li (:value id))}
                  o))
              r))
          r))
      id)))

(defn delete-line-item
  "Delete the line item with given id from the order with given id."
  [sid soid]
  (let [id (string->integer sid)
        oid (string->integer soid)]
    (if (result/ok? id)
      (if (result/ok? oid)
        (let [r (repository/read-order (:value oid))]
          (if (result/ok? r)
            (repository/delete-line-item (:value id) (:value oid))
            r))
        oid)
      id)))
