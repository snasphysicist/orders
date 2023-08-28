(ns orders.add-line-item-test
  (:require [clojure.test :refer :all])
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [orders.core :as core])
  (:require [orders.shared-test :as shared]))

(deftest add-line-item-existing-order
  (testing "Adding a valid line item to an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-08-07"})]
      (do
        (is
          (= 200
            (:status (shared/add-line-item {:product "Blueberry" :quantity 10} oid))))
        (s)))))

(deftest add-line-item-non-existing-order
  (testing "Adding a valid line item to an order that does not exist"
    (let [s (core/-main)]
      (do
        (is
          (= 404
            (:status (shared/add-line-item {:product "Blueberry" :quantity 10} -123))))
        (s)))))

(deftest add-line-item-invalid-order-id
  (testing "Adding a valid line item to an order id that's not an integer"
    (let [s (core/-main)]
      (do
        (is
          (= 400
            (:status (shared/add-line-item {:product "Blueberry" :quantity 10} "foo"))))
        (s)))))

(deftest add-line-item-no-product
  (testing "Adding a line item without a product to an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-08-07"})]
      (do
        (is
          (= 400
            (:status (shared/add-line-item {:quantity 10} oid))))
        (s)))))

(deftest add-line-item-no-quantity
  (testing "Adding a line item without a quantity to an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-08-07"})]
      (do
        (is
          (= 400
            (:status (shared/add-line-item {:product "Strawberry"} oid))))
        (s)))))

(deftest add-line-item-product-invalid-type
  (testing "Adding a line item with a product field of the wrong type to an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-08-07"})]
      (do
        (is
          (= 400
            (:status (shared/add-line-item {:product 123 :quantity 12} oid))))
        (s)))))

(deftest add-line-item-quantity-invalid-type
  (testing "Adding a line item with a quantity field of the wrong type to an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-08-07"})]
      (do
        (is
          (= 400
            (:status (shared/add-line-item {:product "Tomato" :quantity "12"} oid))))
        (s)))))

