(ns workshop.exercise2-test
  (:require
    [clojure.test :refer :all]
    [conjure.core :refer :all]
    [ring.mock.request :as mock]
    [clojure.tools.logging :as log]
    [workshop.exercise1 :as s1]
    [workshop.exercise2 :refer :all]))

(deftest access-log
  (stubbing [s1/handler {:status 200}
             log/log* nil]
    (let [rq (mock/request :get "/")
          rs (handler rq)]
      (is (= 200 (:status rs)))
      (verify-call-times-for log/log* 2))))
