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
    (is (= {:duct.logger/timbre
            {:level :info
             :appenders
             {:duct.logger.timbre/println (ig/ref :duct.logger.timbre/println)}}
            :duct.logger.timbre/println {}}
           (core/build-config base-config))))

  (testing "config with log level"
    (let [config (assoc base-config :duct.profile/base {:duct/logger {:level :warn}})]
      (is (= {:duct.logger/timbre
              {:level :warn
               :appenders
               {:duct.logger.timbre/println (ig/ref :duct.logger.timbre/println)}}
              :duct.logger.timbre/println {}}
             (core/build-config config)))))

  (testing "development environment"
    (let [config (assoc base-config
                        :duct.profile/base
                        {:duct.core/environment :development})]
      (is (= {:duct.logger/timbre
              {:level :debug
               :appenders
               {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
              :duct.logger.timbre/spit  {:fname "logs/dev.log"}
              :duct.logger.timbre/brief {:min-level :report}
              :duct.core/environment :development}
             (core/build-config config)))))

  (testing "environment override"
    (let [config {:duct.module/logging {:environment :development}}]
      (is (= {:duct.logger/timbre
              {:level :debug
               :appenders
               {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
              :duct.logger.timbre/spit  {:fname "logs/dev.log"}
              :duct.logger.timbre/brief {:min-level :report}}
             (core/build-config config)))))

  (testing "config with min log level and file name"
    (let [config (assoc base-config
                        :duct.profile/base
                        {:duct.core/environment :development
                         :duct.logger.timbre/spit  {:fname "custom.log"}
                         :duct.logger.timbre/brief {:min-level :info}})]
      (is (= {:duct.logger/timbre
              {:level :debug
               :appenders
               {:duct.logger.timbre/spit  (ig/ref :duct.logger.timbre/spit)
                :duct.logger.timbre/brief (ig/ref :duct.logger.timbre/brief)}}
              :duct.logger.timbre/spit  {:fname "custom.log"}
              :duct.logger.timbre/brief {:min-level :info}
              :duct.core/environment :development}
             (core/build-config config))))))
