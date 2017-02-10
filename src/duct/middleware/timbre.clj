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
    (let [request-log (select-keys request [:request-method :uri :query-string])]
      (timbre/log* config :info ::request request-log))
    (handler request)))

(defmethod ig/init-key ::binding [_ {:keys [config]}]
  #(wrap-binding % config))

(defmethod ig/init-key ::request-logging [_ {:keys [config]}]
  #(wrap-request-logging % config))
