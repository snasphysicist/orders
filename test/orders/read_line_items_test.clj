(ns orders.read-line-items-test
  (:require [clojure.test :refer :all])
  (:require [orders.core :as core])
  (:require [orders.shared-test :as shared]))

(deftest read-line-items-with-order
  (testing "Line items being returned along with an order"
    (let [s (core/-main)
          oid (shared/create-order-read-id {:customer "Scott" :date "2019-01-22"})]
     (do
       (shared/add-line-item-read-id {:product "Carrot" :quantity 102} oid)
       (shared/add-line-item-read-id {:product "Parsley" :quantity 1} oid)
       (is
         (= [
              {:product "Carrot" :quantity 102}
              {:product "Parsley" :quantity 1}] 
            (map 
              #(select-keys % [:product :quantity])
              (:line-items (:order (shared/read-order oid))))))
       (s)))))
