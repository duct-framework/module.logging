(ns duct.module.logging-test
  (:require [clojure.test :refer [deftest is]]
            [duct.module.logging :as logging]
            [integrant.core :as ig]))

(deftest module-test
  (let [config {:duct.module/logging {}}]
    (is (= {:duct.logger/simple
            {:appenders [{:type :stdout}]}}
           (ig/expand config (ig/deprofile [:main]))))
    (is (= {:duct.logger/simple
            {:appenders [{:type :file, :path "logs/test.log"}]}}
           (ig/expand config (ig/deprofile [:test]))))
    (is (= {:duct.logger/simple
            {:appenders
             [{:type :stdout, :brief? true, :levels #{:report}}
              {:type :file, :path "logs/repl.log"}]}}
           (ig/expand config (ig/deprofile [:repl]))))))
