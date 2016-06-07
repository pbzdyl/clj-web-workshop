(ns workshop.utils
  (:require [migratus.core :as migratus])
  (:import (java.util UUID)))

(defn implement-me! []
  (throw (UnsupportedOperationException. "I'm not implemented yet!")))

(defn random-id []
  (.toString (UUID/randomUUID)))

(defn build-task-url [request id]
  (format "%s://%s:%s%s/%s"
          (name (:scheme request))
          (:server-name request)
          (:server-port request)
          "/api/tasks"
          id))

(def db-conn
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:tasks;DB_CLOSE_DELAY=-1"})

(def migration-config
  {:store         :database
   :migration-dir "migrations"
   :db            db-conn})

(defn migrate-db! []
  @(future
     (migratus/migrate migration-config)))

(defn reset-db! []
  @(future
     (migratus/reset migration-config)))

(defn with-clean-db [f]
  (reset-db!)
  (f))
