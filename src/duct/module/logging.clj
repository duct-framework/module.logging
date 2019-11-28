(ns duct.module.logging
  (:require [duct.core :as core]
            [duct.core.merge :as merge]
            [integrant.core :as ig]))

(defn- get-environment [config options]
  (:environment options (::core/environment config :production)))

(def ^:private prod-config
  {:duct.logger/timbre
   {:level     (merge/displace :info)
    :appenders ^:displace {:duct.logger.timbre/println (ig/ref :duct.logger.timbre/println)}}
   :duct.logger.timbre/println {}})

(def ^:private test-config
  {:duct.logger/timbre
   {:level     (merge/displace :debug)
    :appenders ^:displace {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)}}
   :duct.logger.timbre/spit
   {:fname (merge/displace "logs/test.log")}})

(def ^:private dev-config
  {:duct.logger/timbre
   {:level     (merge/displace :debug)
    :appenders ^:displace {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                           :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
   :duct.logger.timbre/spit
   {:fname (merge/displace "logs/dev.log")}
   :duct.logger.timbre/brief
   {:min-level (merge/displace :report)}})

(def ^:private env-configs
  {:production prod-config
   :development dev-config
   :test       test-config})

(defmethod ig/init-key :duct.module/logging [_ options]
  #(core/merge-configs % (env-configs (get-environment % options))))
