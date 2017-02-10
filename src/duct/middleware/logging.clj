(ns duct.middleware.logging
  (:require [duct.core.protocols :as p]
            [integrant.core :as ig]))

(def ^:private request-log-keys
  [:request-method :uri :query-string])

(defn wrap-request-logging
  "Log each request using the supplied logger. The logger must implement the
  duct.core.protocols/Logger protocol."
  [handler logger]
  (fn [request]
    (p/log logger :info ::request (select-keys request request-log-keys))
    (handler request)))

(defmethod ig/init-key ::request-logging [_ {:keys [config]}]
  #(wrap-request-logging % config))
