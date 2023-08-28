(ns orders.http.endpoint
  (:require [clojure.tools.logging :as log])
  (:require [compojure.core :as compojure])
  (:require [compojure.route :as route])
  (:require [org.httpkit.server :as httpkit])
  (:require [orders.domain.service :as service])
  (:require [orders.errors.errors :as errors])
  (:require [orders.dates.dates :as dates])
  (:require [orders.result.result :as result])
  (:require [orders.json.json :as json]))

(defn- not-found 
  "Returns a useful message for an unknown route."
  [req]
  {:status 404
   :body (str "Not found: " (:uri req) "\n")})

(defn- ->http-response
  "Converts a result to a HTTP response with an appropriate status code."
  [r]
  (if (result/ok? r)
    {:status 200 :body (json/write-str (:value r))}
    {:status (errors/http-code-for (:type r)) :body (json/write-str r)}))

(defn- create-order 
  "Offer create order functionality through the HTTP endpoint."
  [r]
  (let [o (json/read-str (slurp (:body r)))]
    (->http-response (service/create-order o))))

(defn- read-order 
  "Allow an order to be read through the HTTP endpoint."
  [id r]
  (let [r (service/read-order id)]
    (if (result/ok? r)
      (do
        {:status 200 :body (json/write-str (:value r))})
      {:status (errors/http-code-for (:type r)) :body (json/write-str r)})))

(defn- delete-order
  "Allow an order to be deleted through the HTTP endpoint."
  [id r]
  (->http-response (service/delete-order id)))

(defn- add-line-item
  "Allow a line item to be added to an order."
  [id r]
  (let [li 
         (json/read-str (slurp (:body r)))] 
    (->http-response (service/add-line-item li id))))

(defn- delete-line-item
  "Allow a line item to be deleted from an order."
  [id oid r]
  (->http-response (service/delete-line-item id oid)))

(compojure/defroutes r
  (compojure/POST "/order" [] create-order)
  (compojure/GET "/order/:id" [id] (partial read-order id))
  (compojure/DELETE "/order/:id" [id] (partial delete-order id))
  (compojure/POST "/order/:id/item" [id] (partial add-line-item id))
  (compojure/DELETE "/order/:oid/item/:id" [oid id] (partial delete-line-item id oid))
  (route/not-found not-found))

(defn set-up-server 
  "Starts the HTTP server with the configured routes on the given port."
  [port] 
  (httpkit/run-server r {:port port}))
