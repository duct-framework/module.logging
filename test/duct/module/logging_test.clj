(ns duct.module.logging-test
  (:require [clojure.test :refer :all]
            [duct.core :as core]
            [duct.module.logging :as logging]
            [integrant.core :as ig]))

(core/load-hierarchy)

(def base-config
  {:duct.module/logging {}})

(deftest module-test
  (testing "blank config"
    (is (= (core/prep base-config)
           (merge base-config
                  {:duct.logger/timbre
                   {:level :info
                    :appenders
                    {:duct.logger.timbre/println (ig/ref :duct.logger.timbre/println)}}
                   :duct.logger.timbre/println {}}))))

  (testing "config with log level"
    (let [config (assoc base-config :duct/logger {:level :warn})]
      (is (= (core/prep config)
             (merge base-config
                    {:duct.logger/timbre
                     {:level :warn
                      :appenders
                      {:duct.logger.timbre/println (ig/ref :duct.logger.timbre/println)}}
                     :duct.logger.timbre/println {}})))))

  (testing "development environment"
    (let [config (assoc base-config ::core/environment :development)]
      (is (= (core/prep config)
             (merge config
                    {:duct.logger/timbre
                     {:level :debug
                      :appenders
                      {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                       :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
                     :duct.logger.timbre/spit  {:fname "logs/dev.log"}
                     :duct.logger.timbre/brief {:min-level :report}})))))

  (testing "config with min log level and file name"
    (let [config (assoc base-config
                        ::core/environment :development
                        :duct.logger.timbre/brief {:min-level :info}
                        :duct.logger.timbre/spit  {:fname "custom.log"})]
      (is (= (core/prep config)
             (merge config
                    {:duct.logger/timbre
                     {:level :debug
                      :appenders
                      {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                       :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
                     :duct.logger.timbre/spit  {:fname "custom.log"}
                     :duct.logger.timbre/brief {:min-level :info}}))))))
