(ns workshop.exercise1-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :as str]
    [workshop.exercise1 :refer :all]))

(deftest echo-handler
  (let [rq {:uri "/echo" :request-method :post :body "Hi"}
        rs (handler rq)]
    (is (= 200 (:status rs)))
    (is (str/includes? (:body rs) ":body \"Hi\""))
    (is (str/includes? (:body rs) ":uri \"/echo\""))
    (is (str/includes? (:body rs) ":request-method :post"))))
