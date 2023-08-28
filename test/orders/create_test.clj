(ns orders.create-test
  (:require [clojure.test :refer :all])
  (:require [orders.shared-test :as shared])
  (:require [orders.core :as core]))

(deftest create-new-order
  (testing "Create a new order successfully"
    (let [s (core/-main)]
      (do
        (is
          (= 200 
            (:status (shared/create-order {:customer "Scott" :date "2023-08-18"}))))
        (s)))))

(deftest create-new-order-returns-id
  (testing "Creating a new order returns the id"
    (let [s (core/-main)]
      (do
        (is
          (= 200 
              (:status (shared/create-order {:customer "Scott" :date "2023-08-18"}))))
          (s)))))

(deftest create-new-order-invalid-date
  (testing "Attempt to create a new order with an invalid date"
    (let [s (core/-main)]
        (do
          (is   
            (not= nil
              (shared/create-order-read-id 
                {:customer "Scott" 
                 :date "2023-07-22"})))
        (s)))))

(deftest create-new-order-missing-customer
  (testing "Attempt to create a new order without a customer"
    (let [s (core/-main)]
      (do
        (is   
          (= 400 
            (:status 
              (shared/create-order {:date "2023-07-22"}))))
      (s)))))

(deftest create-new-order-missing-date
  (testing "Attempt to create a new order without a date"
    (let [s (core/-main)]
      (do
        (is   
          (= 400 
            (:status 
              (shared/create-order {:customer "Scott"}))))
      (s)))))

(deftest create-new-order-customer-wrong-type
  (testing "Attempt to create a new order a non-string customer"
    (let [s (core/-main)]
      (do
        (is   
          (= 400 
            (:status 
              (shared/create-order {:customer 123 :date "2023-07-22"}))))
      (s)))))

(deftest create-new-order-date-wrong-type
  (testing "Attempt to create a new order a non-string date"
    (let [s (core/-main)]
      (do
        (is   
          (= 400 
            (:status 
              (shared/create-order {:customer "Scott" :date 123}))))
      (s)))))
