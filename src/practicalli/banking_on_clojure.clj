(ns practicalli.banking-on-clojure
  (:gen-class)
  (:require
   [org.httpkit.server :as app-server]
   [ring.middleware.json :refer [wrap-json-response]]
   [compojure.core :refer [defroutes POST]]
   [practicalli.request-handler :as handler]))

(use '[ring.middleware.json :only [wrap-json-body]])
;; Request Routing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes handler
  (POST "/charge" [] handler/post-charge))

(def app
  (-> handler
      wrap-json-response
      wrap-json-body))


;; System
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Reference to application server instance for stopping/restarting
(defonce app-server-instance (atom nil))


(defn app-server-start
  "Start the application server and log the time of start."
  [http-port]
  (println (str (java.util.Date.)
                " INFO: Starting server on port: " http-port))
  (reset! app-server-instance
          (app-server/run-server #'app {:port http-port})))


(defn app-server-stop
  "Gracefully shutdown the server, waiting 100ms.  Log the time of shutdown."
  []
  (when-not (nil? @app-server-instance)
    (@app-server-instance :timeout 100)
    (reset! app-server-instance nil)
    (println (str (java.util.Date.)
                  " INFO: Application server shutting down..."))))


(defn app-server-restart
  "Convenience function to stop and start the application server"
  [http-port]
  (app-server-stop)
  (app-server-start http-port))


(defn -main
  "Start web server to listen on port 8888."

  [& [http-port]]
  (let [http-port (Integer. (or http-port (System/getenv "PORT") "8888"))]
    (app-server-start http-port)))


;; REPL driven development helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment

  ;; Start application server - via `-main` or `app-server-start`
  (-main)
  (app-server-start 8888)

  ;; Stop / restart application server
  (app-server-stop)
  (app-server-restart 8888)

  ;; Get PORT environment variable from Operating System
  (System/getenv "PORT")

  ;; Get all environment variables
  ;; use a data inspector to view environment-variables name
  (def environment-variables
    (System/getenv))

  ;; Check values set in the default system properties
  (def system-properties
    (System/getProperties)))
