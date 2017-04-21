(ns duct.module.logging
  (:require [duct.core :as core]
            [duct.core.merge :as merge]
            [duct.logger :as logger]
            [duct.logger.timbre :as timbre]
            [integrant.core :as ig]))

(defn- get-environment [config options]
  (:enviroment options (::core/environment config :production)))

(def ^:private prod-config
  {::logger/timbre
   {:level     (merge/displace :info)
    :appenders ^:displace {::timbre/println (ig/ref ::timbre/println)}}
   ::timbre/println {}})

(def ^:private dev-config
  {::logger/timbre
   {:level     (merge/displace :debug)
    :appenders ^:displace {::timbre/spit  (ig/ref ::timbre/spit)
                           ::timbre/brief (ig/ref ::timbre/brief)}}
   ::timbre/spit
   {:fname (merge/displace "logs/dev.log")}
   ::timbre/brief {:min-level (merge/displace :report)}})

(def ^:private env-configs
  {:production prod-config
   :development dev-config})

(defmethod ig/init-key :duct.module/logging [_ options]
  (fn [config]
    (core/merge-configs config (env-configs (get-environment config options)))))
