(ns duct.logger.timbre
  (:require [duct.core.protocols :as p]
            [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(defmethod ig/init-key ::println [_ options]
  (timbre/println-appender options))

(defmethod ig/init-key ::spit [_ options]
  (timbre/spit-appender options))

(derive :duct.logger/timbre :duct/logger)

(defrecord TimbreLogger [config]
  p/Logger
  (log [_ level key]      (timbre/log* config level key))
  (log [_ level key data] (timbre/log* config level key data))
  (log-ex [_ level ex]    (timbre/log* config level ex)))

(defmethod ig/init-key :duct.logger/timbre [_ config]
  (->TimbreLogger config))
