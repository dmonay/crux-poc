(ns practicalli.hanlder-helpers
  (:require
   [practicalli.data.access :refer [create-record]]))


;; Create

(defn post-charge
  [charge-json]
  (prn charge-json
    ;; create-record
       ))


;; (defn new-customer
;;   [customer-details]
;;   (create-record connection/db-spec-dev :public.customer customer-details))


