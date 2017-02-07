(ns duct.middleware.timbre
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(defn wrap-binding
  "Wrap the handler with a Timbre logger binding using the specific logging
  config."
  [handler config]
  (fn [request]
    (timbre/with-config config
      (handler request))))

(defn wrap-request-logging
  "Log each request using the specified logging config."
  [handler config]
  (fn [request]
    (timbre/log* config :info [(:request-method request) (:uri request)])
    (handler request)))

(defmethod ig/init-key ::binding [_ config]
  #(wrap-binding % config))

(defmethod ig/init-key ::request-logging [_ config]
  #(wrap-request-logging % config))
