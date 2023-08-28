(ns orders.shared-test
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(defn json-read-str 
  "Parse a JSON string and on exception rethrow with the full input."
  [s]
  (try 
    (json/read-str s :key-fn keyword)
    (catch Exception e (throw (Exception. (str e " from JSON '" s "'"))))))

(defn- not-nil
  "Raises if the input is nil."
  [i & msg]
  (if (nil? i)
    (throw (Exception. (str "Input was nil - context: " msg)))
    i))

(defn create-order 
  "Create an order with the provided details in the service."
  [o]
  (client/post "http://localhost:3000/order" {:body (json/write-str o) :throw-exceptions false}))

(defn create-order-read-id 
  "Create an order with the provided details in the service, returning its id."
  [o]
  (let [res (create-order o)]
    (not-nil (:id (json/read-str (:body res) :key-fn keyword)) res)))

(defn add-line-item
  "Add a line item to the order with the given id in the service API."
  [li id]
  (client/post 
    (str "http://localhost:3000/order/" id "/item") 
    {:body (json/write-str li)
     :throw-exceptions false}))

(defn add-line-item-read-id
  "Add a line item to the order with the given id in the service and return its id."
  [li id]
  (let [res (add-line-item li id)]
    (not-nil (:id (json-read-str (:body res))) res)))
