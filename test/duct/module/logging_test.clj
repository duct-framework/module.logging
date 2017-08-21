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
                     ::timbre/brief {:min-level :report}})))))

  (testing "config with min log level and file name"
    (let [config (assoc base-config
                        ::core/environment :development
                        ::timbre/brief {:min-level :info}
                        ::timbre/spit  {:fname "custom.log"})]
      (is (= (core/prep config)
             (merge config
                    {:duct.logger/timbre
                     {:level     :debug
                      :appenders {::timbre/spit  (ig/ref ::timbre/spit)
                                  ::timbre/brief (ig/ref ::timbre/brief)}}
                     ::timbre/spit  {:fname "custom.log"}
                     ::timbre/brief {:min-level :info}}))))))
