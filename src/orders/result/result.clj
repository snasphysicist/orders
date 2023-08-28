(ns orders.result.result)

(defn ok? 
  "Returns true if the result is the ok/success variant."
  [r] 
  (= (:result r) :ok))

(defn error? 
  "Returns true if the result if the error/failure variant."
  [r] 
  (= (:result r) :error))
