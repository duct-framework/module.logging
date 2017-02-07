(ns duct.module.logging
  (:require [integrant.core :as ig]
            [duct.core :refer [assoc-in-default]]
            [duct.logger.timbre :as timbre]
            [duct.middleware.timbre :as mw]
            [duct.module.web :as web]
            [meta-merge.core :refer [meta-merge]]))

(defn add-appender [config key options]
  (-> config
      (assoc-in-default [:duct.logger/timbre :appenders key] (ig/ref key))
      (update key (partial meta-merge options))))

(defmethod ig/init-key :duct.module/logging [_ options]
  (fn [config]
    (let [env (:enviroment options (:duct.core/environment config :production))]
      (-> config
          (assoc-in-default [:duct.logger/timbre :level] :info)
          (cond->
            (= env :production)  (add-appender ::timbre/println {:stream :auto})
            (= env :development) (add-appender ::timbre/spit    {:fname "logs/dev.log"}))
          (web/add-middleware ::mw/binding         (ig/ref :duct.logger/timbre))
          (web/add-middleware ::mw/request-logging (ig/ref :duct.logger/timbre))))))
