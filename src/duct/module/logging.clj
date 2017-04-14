(ns duct.module.logging
  (:require [duct.core :as core]
            [duct.logger :as logger]
            [duct.logger.timbre :as timbre]
            [integrant.core :as ig]))

(defn- get-environment [config options]
  (:enviroment options (::core/environment config :production)))

(defn- set-default-log-level [config level]
  (core/merge-configs config {::logger/timbre ^:displace {:level level}}))

(defn- add-appender [config key options]
  (core/merge-configs config {::logger/timbre {:appenders {key (ig/ref key)}}
                              key options}))

(defmethod ig/init-key :duct.module/logging [_ options]
  (fn [config]
    (case (get-environment config options)
      :production
      (-> config
          (set-default-log-level :info)
          (add-appender ::timbre/println {}))
      :development
      (-> config
          (set-default-log-level :debug)
          (add-appender ::timbre/spit  {:fname "logs/dev.log"})
          (add-appender ::timbre/brief {})))))
