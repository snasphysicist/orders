(ns orders.delete-test
  (:require [clojure.test :refer :all])
  (:require [clj-http.client :as client])
  (:require [orders.shared-test :as shared])
  (:require [orders.core :as core]))

(defn- delete-order
  "Delete the order with the given id from the service."
  [id]
  (client/delete 
    (str "http://localhost:3000/order/" id)
    {:throw-exceptions false}))

(deftest delete-existing-order
  (testing "Delete an order that has been created"
    (let [s (core/-main)
          id (shared/create-order-read-id 
               {:customer "Scott" 
                :date "2023-06-09"})]
      (do
        (is 
          (= 200
             (:status (delete-order id))))
        (s)))))

(deftest delete-order-which-does-not-exist
  (testing "Delete an order that has not been created"
    (let [s (core/-main)]
      (do
        (is 
          (= 404
             (:status (delete-order -123))))
        (s)))))

(deftest delete-order-with-invalid-id
  (testing "Attempts to delete an order with a invalid id type"
    (let [s (core/-main)]
      (do
        (is 
          (= 400
             (:status (delete-order "foo"))))
        (s)))))
