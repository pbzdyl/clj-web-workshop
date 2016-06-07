(ns workshop.exercise5-test
  (:require
    [clojure.test :refer :all]
    [workshop.utils :refer :all]
    [workshop.exercise5 :refer :all]))

(use-fixtures :each with-clean-db)

(deftest tasks-db
  (testing "querying empty db"
    (is (empty? (fetch-tasks))))

  (testing "task creation"
    (let [t1 (create-task! {:content "Buy some milk" :done false})]

      (is (= (dissoc t1 :id)
             {:content "Buy some milk"
              :done    false}))
      (is (contains? t1 :id))

      (testing "task lookup"
        (is (nil? (fetch-task "unknown-id")))

        (is (= (fetch-task (:id t1))
               t1)))

      (is (= (fetch-tasks)
             [t1]))

      (testing "changing task done status"
        (is (true? (-> t1
                       :id
                       (set-task-done-status! true)
                       :done)))
        (is (false? (-> t1
                        :id
                        (set-task-done-status! false)
                        :done))))

      (testing "task updates"
        (let [t1' (update-task! (:id t1) (assoc t1 :content "Buy more milk" :done true))]

          (is (= t1'
                 {:id      (:id t1)
                  :content "Buy more milk"
                  :done    true}))

          (is (= (fetch-tasks)
                 [t1']))))

      (testing "task deletion"
        (is (nil? (delete-task! (:id t1))))
        (is (empty? (fetch-tasks)))))))
