(ns orders.delete-line-item-test
  (:require [clojure.test :refer :all])
  (:require [clj-http.client :as client])
  (:require [orders.core :as core])
  (:require [orders.shared-test :as shared]))

(defn- delete-line-item
  "Removes a line item from an order in the service API."
  [id oid]
  (client/delete
    (str "http://localhost:3000/order/" oid "/item/" id)
    {:throw-exceptions false}))

(deftest delete-line-item-existing-order-line-item
  (testing "Deleting a line item that exists from an order that exists"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2021-09-30"})
          id (shared/add-line-item-read-id {:product "Pear" :quantity 10} oid)]
      (do
        (is
          (= 200
            (:status (delete-line-item id oid))))
        (s)))))

(deftest delete-line-item-non-existing-order
  (testing "Attempting to delete a line item from an order that does not exist"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2021-09-30"})
          id (shared/add-line-item-read-id {:product "Pear" :quantity 10} oid)]
      (do
        (is
          (= 404
            (:status (delete-line-item id -123))))
        (s)))))

(deftest delete-line-item-non-existing-line-item
  (testing "Attempting to delete a line item that does not exist from an order that does exist"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2021-07-30"})
          id (shared/add-line-item-read-id {:product "Mango" :quantity 12} oid)]
      (do
        (is
          (= 404
            (:status (delete-line-item -123 oid))))
        (s)))))

(deftest delete-line-item-order-id-invalid-type
  (testing "Attempting to delete a line item with an order id of invalid type"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-07-30"})
          id (shared/add-line-item-read-id {:product "Mangosteen" :quantity 12} oid)]
      (do
        (is
          (= 400
            (:status (delete-line-item id "foo"))))
        (s)))))

(deftest delete-line-item-line-item-id-invalid-type
  (testing "Attempting to delete a line item with an id of an invalid type"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2022-07-30"})
          id (shared/add-line-item-read-id {:product "Mangosteen" :quantity 12} oid)]
      (do
        (is
          (= 400
            (:status (delete-line-item "bar" oid))))
        (s)))))
