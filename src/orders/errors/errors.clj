(ns orders.errors.errors)

(def not-found 
  "Error 'type' representing a resource that could not be found."
   "not-found")

(def bad-input
  "Error 'type' representing a bad input from a client."
  "bad-input")

(defn- http-codes [] {not-found 404 bad-input 400})

(defn http-code-for
  "Returns an appropriate http code for each error type."
  [e]
  (second (first (concat (filter (fn [[k v]] (= k e)) (http-codes)) '(["internal" 500])))))
