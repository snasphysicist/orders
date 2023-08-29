(ns orders.read-test
  (:require [clojure.test :refer :all])
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as client])
  (:require [orders.shared-test :as shared])
  (:require [orders.core :as core]))

(deftest read-back-order
  (testing "Read back an order"
    (let [s (core/-main)]
      (do
        (is 
          (= {:customer "Scott" :date "2023-08-19"}
             (let [id (shared/create-order-read-id {:customer "Scott" :date "2023-08-19"})]
               (select-keys (:order (shared/read-order id)) [:customer :date]))))
        (s)))))

(deftest read-back-non-existent-order
  (testing "Read back an order that does not exist"
    (let [s (core/-main)]
      (do
        (is 
          (= 404 (:status (shared/read-order -123))))
        (s)))))

(deftest read-order-invalid-id
  (testing "Attempt to read an order with an invalid id type"
    (let [s (core/-main)]
      (do
        (is 
          (= 400 (:status (shared/read-order "foo"))))
        (s)))))
