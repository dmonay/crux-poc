(ns practicalli.data.access
  (:require
   [crux.api :as crux]
  ;;  [clojure.spec.alpha     :as spec]
  ;;  [clojure.spec.gen.alpha :as spec-gen]
   ))

   ;; Data access
  ;;  [practicalli.data.specs :as schema]))

;; MAIN CRUX IN-MEMORY NODE
(def crux
  (crux/start-node {}))



(defn create-record
  "Insert a single record into the database.
  Arguments:
   - data: raw charge input
   - account_id: account_id"

  [charge-json account_id]

  (do
    (println "SERVICE LAYER: create-record" charge-json)

    (println "SERVICE LAYER: account_id is:  " account_id)

    (def charge1
      {:crux.db/id (java.util.UUID/randomUUID)
       :account-id account_id
       :product-id "f0b06263-73c3-44a8-a261-ec0e419ef089"
       :interest-rate (charge-json :rate)
       :amount-cents (charge-json :amount_cents)})


    (crux/await-tx crux
                   (crux/submit-tx crux [[:crux.tx/put charge1]]))))


