(ns duct.module.logging-test
  (:require [clojure.test :refer :all]
            [duct.core :as core]
            [duct.module.logging :as logging]
            [duct.logger.timbre :as timbre]
            [integrant.core :as ig]))

(def base-config
  {:duct.core/modules   [(ig/ref :duct.module/logging)]
   :duct.module/logging {}})

(deftest module-test
  (testing "blank config"
    (is (= (core/prep base-config)
           (merge base-config
                  {:duct.logger/timbre
                   {:level     :info
                    :appenders {::timbre/println (ig/ref ::timbre/println)}}
                   ::timbre/println {}}))))

  (testing "config with log level"
    (let [config (assoc base-config :duct/logger {:level :warn})]
      (is (= (core/prep config)
             (merge base-config
                    {:duct.logger/timbre
                     {:level     :warn
                      :appenders {::timbre/println (ig/ref ::timbre/println)}}
                     ::timbre/println {}})))))

  (testing "development environment"
    (let [config (assoc base-config ::core/environment :development)]
      (is (= (core/prep config)
             (merge config
                    {:duct.logger/timbre
                     {:level     :debug
                      :appenders {::timbre/spit  (ig/ref ::timbre/spit)
                                  ::timbre/brief (ig/ref ::timbre/brief)}}
                     ::timbre/spit  {:fname "logs/dev.log"}
                     ::timbre/brief {}}))))))
