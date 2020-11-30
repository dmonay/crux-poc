;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Request handlers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns practicalli.request-handler
  (:require

   ;; Web Application
   [ring.util.response :refer [response]]

   ;; Data access
   [practicalli.hanlder-helpers :as helper]))

(defn post-charge [req]
  ;; (prn req)
  (when-let [created-charge (helper/post-charge (req :body) (get-in req [:params :account_id]))]
    (response {:result created-charge})))
