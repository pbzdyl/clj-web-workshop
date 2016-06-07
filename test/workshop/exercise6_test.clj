(ns workshop.exercise6-test
  (:require
    [clojure.test :refer :all]
    [kerodon.core :refer :all]
    [kerodon.test :refer :all]
    [workshop.utils :refer :all]
    [workshop.memdb :refer [clean-db!]]
    [workshop.exercise6 :refer [handler]]))

(use-fixtures :each (fn [t] (clean-db!) (t)))

(deftest tasks-page
  (-> (session handler)
      (visit "/tasks")

      (within [:#tasks-list]
        (has (text? "No tasks")))

      (fill-in "New task:" "Buy some milk")
      (press "Add")
      (follow-redirect)

      (within [:#tasks-list]
        (has (text? "Buy some milk")))

      (fill-in "New task:" "Buy bread")
      (press "Add")
      (follow-redirect)

      (within [:#tasks-list]
        (has (some-text? "Buy some milk"))
        (has (some-text? "Buy bread")))))
