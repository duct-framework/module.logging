(ns duct.middleware.timbre
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(defn wrap-binding
  "Wrap the handler with a Timbre logger binding."
  [handler config]
  (fn [request]
    (timbre/with-config config
      (handler request))))

(defmethod ig/init-key ::binding [_ config]
  #(wrap-binding % config))
