(ns orders.domain.validation
  (:require [orders.errors.errors :as errors])
  (:require [orders.result.result :as result]))

(defn- contains-key?
  "Returns an error if the map does not contain the provided key."
  [m k]
  (if (contains? m k)
    {:result :ok}
    {:result :error 
     :type errors/bad-input 
     :message (str k " missing from input")}))

(defn contains-keys? 
  "Returns an error if the map does not contain all the provided keys."
  [m ks]
  (let [errs (filter result/error? (map (partial contains-key? m) ks))]
    (if (empty? errs)
      {:result :ok}
      {:result :error 
       :type errors/bad-input 
       :message (clojure.string/join ", " (map #(:message %) errs))})))

(defn- is-of-type?
  "Returns an error if the value is not of the type."
  [k v t]
  (if (= (type v) t)
    {:result :ok}
    {:result :error 
     :type errors/bad-input 
     :message (str "Field " k " must be of type " t " not " (type v))}))

(defn- has-type?
  "Returns an error if given key is not present with the given type."
  [m [k t]]
  (is-of-type? k (k m) t))

(defn has-types? 
  "Returns an error if the values corresponding to the given keys are not of the given types.
   Assumes all keys for which types are given will be in the map."
  [m types]
  (let [errs (filter result/error? (map (partial has-type? m) types))]
    (if (empty? errs)
      {:result :ok}
      {:result :error
       :type errors/bad-input
       :message (clojure.string/join ", " (map #(:message %) errs))})))
