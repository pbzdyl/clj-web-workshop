(ns workshop.sql
  (:require
    [workshop.utils :refer :all]
    [yesql.core :refer [defqueries]]))

(migrate-db!)

(defqueries "tasks.sql"
  {:connection db-conn})
