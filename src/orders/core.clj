(ns orders.core
  (:gen-class)  
  (:require [orders.domain.service :as service])
  (:require [orders.http.endpoint :as endpoint]))

(defn -main
  "Starts the service, serving on HTTP."
  [& args]
  (do    
    (service/initialise)
    (endpoint/set-up-server 3000)))
