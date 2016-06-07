(ns workshop.exercise5
  (:require
    [workshop.utils :refer :all]
    [workshop.sql :as sql]
    [clojure.java.jdbc :refer [with-db-transaction]]))

;; Exercise goals
;; Use Yesql to generate SQL functions for you based on resources/tasks.sql file in workshop.sql namespace.
;; Use them in the functions below to implement their contracts.
;;
;; https://github.com/krisajenkins/yesql

(defn fetch-tasks
  "Returns a sequence of all tasks from DB"
  []
  (sql/all-tasks))

(defn fetch-task
  "Returns a task with the given ID or nil if it doesn't exist"
  [id]
  (first (sql/task-by-id {:id id})))

(defn create-task!
  "Saves a task in the DB and returns the saved task (which will include its :id)
   t should be in format {:content String :done boolean}
   The function creates a task :id using workshop.utils/random-id before saving t in the DB"
  [t]
  (let [t (assoc t :id (random-id))]
    (sql/create-task! t)
    t))

(defn update-task!
  "Updates the task in the DB based on the provided t (using the same format as t in create-task function).
   Returns the updated task."
  [id t]
  (let [t (assoc t :id id)]
    (with-db-transaction [tx db-conn]
      (sql/update-task-by-id! t {:connection tx})
      (first (sql/task-by-id {:id id} {:connection tx})))))

(defn delete-task!
  "Deletes a task with provided ID from DB.
   Returns nil"
  [id]
  (sql/delete-task-by-id! {:id id})
  nil)

(defn set-task-done-status!
  "Updates :done property of the task with provided id to the value specified by status.
   Returns updated task"
  [id status]
  (with-db-transaction [tx db-conn]
    (sql/update-task-completion-by-id! {:id id :done status})
    (first (sql/task-by-id {:id id}))))
