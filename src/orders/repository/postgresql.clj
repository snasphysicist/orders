(ns orders.repository.postgresql
  (:require [next.jdbc :as jdbc])
  (:require [next.jdbc.date-time :as dt])
  (:require [clojure.data.json :as json])
  (:require [orders.errors.errors :as errors]))

(defn- configuration 
  "Returns the configuration for the database connection."
  [] 
  {:dbtype "postgres" 
   :dbname "clojure" 
   :user "admin" 
   :password "admin" 
   :host "localhost" 
   :port 5432})

(defn- db 
  "Sets up a connection to the database according to the configuration."
  []
  (jdbc/get-datasource (configuration)))

(defn initialise 
  "Initialises the database, ensuring the required tables are created."
  []
  (dt/read-as-local)
  (jdbc/execute! (db) 
                 [(str "CREATE TABLE IF NOT EXISTS orders (id SERIAL, customer VARCHAR NOT NULL, contents JSONB);"
                       "ALTER TABLE orders DROP COLUMN IF EXISTS contents;"
                       "ALTER TABLE orders ADD COLUMN IF NOT EXISTS odate DATE;"
                       "UPDATE orders SET odate=NOW();"
                       "ALTER TABLE orders ALTER COLUMN odate SET NOT NULL;"
                       "ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_id_unique CASCADE;"
                       "ALTER TABLE orders ADD CONSTRAINT orders_id_unique UNIQUE (id);"
                       "CREATE TABLE IF NOT EXISTS line_items"
                       "    (id SERIAL, oid INT, product VARCHAR, quantity INT);"
                       "ALTER TABLE line_items DROP CONSTRAINT IF EXISTS line_items_oid_foreign_key;"
                       "ALTER TABLE line_items"
                       "    ADD CONSTRAINT line_items_oid_foreign_key"
                       "    FOREIGN KEY (oid)"
                       "    REFERENCES orders(id)"
                       "    ON DELETE CASCADE;")]))

(defn create-order 
  "Create the given order in the database."
  [o]
  (let [sql "INSERT INTO orders (customer, odate) VALUES (?, ?) RETURNING id;"]
    {:id 
      (:orders/id 
         (first 
          (jdbc/execute! (db) [sql (:customer o) (:date o)])))}))

(defn- strip-table-prefix
  "Removes the table prefix :orders/ from the order keys."
  [o]
  (update-keys o
    (fn [k] (keyword (clojure.string/replace (str k) #"^:orders/" "")))))

(defn- update-date-key
  "Replaces the :odate key with :date"
  [o]
  (dissoc (merge o {:date (:odate o)}) :odate))

(defn read-order
  "Read the order with the given id from the database."
  [id]
  (let [sql "SELECT * FROM orders WHERE id=?;"
        o (first (jdbc/execute! (db) [sql (Integer. (str id))]))]
    (if (empty? o)
      {:result :error :type errors/not-found :message (str "No order with id " id)}
      {:result :ok :value (update-date-key (strip-table-prefix o))})))

(defn delete-order
  "Delete the order with the given id from the database."
  [id]
  (let [sql "DELETE FROM orders where id=?;"
        r (jdbc/execute! (db) [sql (Integer. id)])]
    (if (= 1 (:next.jdbc/update-count (first r)))
      {:result :ok}
      {:result :error :type errors/not-found :message (str "No order with id " id)})))

(defn add-line-item
  "Add the given line item for the given order."
  [li id]
  (let [sql "INSERT INTO line_items (oid, product, quantity) VALUES (?, ?, ?) RETURNING id;"]
    {:id
      (:line_items/id 
        (first 
          (jdbc/execute! (db) [sql (Integer. (str id)) (:product li) (:quantity li)])))}))

(defn delete-line-item
  "Remove the line item with the given id from the database."
  [id oid]
  (let [sql "DELETE FROM line_items WHERE id=? AND oid=?;"
        r (jdbc/execute! (db) [sql (Integer. id) (Integer. oid)])]
    (if (= 1 (:next.jdbc/update-count (first r)))
      {:result :ok}
      {:result :error
       :type errors/not-found
       :message (str "No line item in order " oid  " with id " id)})))
