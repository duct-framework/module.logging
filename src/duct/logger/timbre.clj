(ns duct.logger.timbre
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(defmethod ig/init-key ::println [_ options]
  (timbre/println-appender options))

(defmethod ig/init-key ::spit [_ options]
  (timbre/spit-appender options))

(derive :duct.logger/timbre :duct/logger)

(defmethod ig/init-key :duct.logger/timbre [_ config]
  config)
