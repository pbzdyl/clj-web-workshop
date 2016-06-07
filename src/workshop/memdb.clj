(ns workshop.memdb
  (:require [workshop.utils :refer :all]))

(defonce ^:private tasks-db (atom {}))

(defn fetch-tasks []
  (or (vals @tasks-db)
      []))

(defn fetch-task [id]
  (@tasks-db id))

(defn create-task! [t]
  (let [id (random-id)
        t (assoc t :id id)]
    (swap! tasks-db assoc id t)
    t))

(defn update-task! [id t]
  (let [t (assoc t :id id)]
    (swap! tasks-db assoc id t)
    t))

(defn delete-task! [id]
  (swap! tasks-db dissoc id)
  nil)

(defn set-task-done-status! [id status]
  (swap! tasks-db assoc-in [id :done] status)
  (fetch-task id))

(defn clean-db! []
  (reset! tasks-db {}))
