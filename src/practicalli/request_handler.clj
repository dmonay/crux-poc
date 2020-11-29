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
  ;; (prn (req :body))
  (helper/post-charge (req :body))
  (response {:foo "bar"}))
