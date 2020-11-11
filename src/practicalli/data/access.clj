(ns practicalli.data.access
  (:require
   [crux.api :as crux]))

;; MAIN CRUX IN-MEMORY NODE
(def crux
  (crux/start-node {}))

;; SAMPLE ENTITY SCHEMA
(def manifest
  {:crux.db/id :manifest
   :pilot-name "Johanna"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})


(defn create-record
  "Insert a single record into the database .
  Arguments:"

  []

  (do
    (println "SERVICE LAYER: create-record")
    (crux/submit-tx crux [[:crux.tx/put manifest]])))
;; => #'practicalli.data.access/create-record

  ;; (crux/entity (crux/db crux) :manifest))
  ; (crux/entity-history (crux/db crux) :manifest :asc))






(defn read-record
  "Insert a single record into the database using a managed connection.
  Arguments:
  - table - name of database table to be affected
  - record-data - Clojure data representing a new record
  - db-spec - database specification to establish a connection"
  ; [db-spec sql-query]
  []
  (crux/entity (crux/db crux) :manifest))


; (defn update-record
;   "Insert a single record into the database using a managed connection.
;   Arguments:
;   - table - name of database table to be affected
;   - record-data - Clojure data representing a new record
;   - db-spec - database specification to establish a connection
;   - where-clause - column and value to identify a record to update"
;   [db-spec table record-data where-clause]
;   (with-open [connection (jdbc/get-connection db-spec)]
;     (jdbc-sql/update!
;      connection
;      table
;      record-data
;      where-clause
;      jdbc/snake-kebab-opts)))
