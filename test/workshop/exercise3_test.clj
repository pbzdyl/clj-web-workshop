(ns workshop.exercise3-test
  (:require
    [clojure.test :refer :all]
    [workshop.utils :refer :all]
    [workshop.memdb :refer [clean-db!]]
    [workshop.exercise3 :refer :all]))

(use-fixtures :each (fn [t] (clean-db!) (t)))

(deftest routes
  (let [rq {:uri "/api/tasks" :request-method :get}
        rs (app-routes rq)]
    (is (empty?  (:body rs))))

  (let [rq (-> {:uri            "/api/tasks"
                :request-method :post
                :body           {:content "Buy some milk" :done false}})
        rs (app-routes rq)
        task (:body rs)]
    (is (= (:content task)
           "Buy some milk"))
    (is (false? (:done task)))
    (is (contains? task :id))))
