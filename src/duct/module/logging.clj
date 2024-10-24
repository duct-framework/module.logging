(ns duct.module.logging
  (:require [integrant.core :as ig]))

(defmethod ig/expand-key :duct.module/logging [_ _]
  {:duct.logger/simple
   (ig/profile
    :dev  {:appenders
           [{:type :stdout, :timestamps? false, :levels #{:report}}
            {:type :file, :path "logs/dev.log"}]}
    :test {:appenders
           [{:type :file, :path "logs/test.log"}]}
    :prod {:appenders
           [{:type :stdout}]})})
