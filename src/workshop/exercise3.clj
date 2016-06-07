(ns workshop.exercise3
  (:require
    [workshop.utils :refer :all]
    [workshop.exercise2 :refer [wrap-access-log]]
    ;; FIXME memdb -> exercise5
    [workshop.memdb :as db]
    [ring.util.http-response :as rs]
    [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
    [compojure.core :refer :all]))

;; Exercise goals
;; Write following endpoints for Task REST resource:
;; - GET /tasks - should return a sequence of existing tasks
;; - POST /tasks - should create a new task based on the request's body content using db/create-task
;;                 and return the newly created task
;; - GET /tasks/:id - should return HTTP 200 with existing task with specified id by using (db/task id)
;;                    or HTTP 404 if task with the id doesn't exist
;; - PUT /tasks/:id - should updated a task with id with the data passed in the request's body using db/update-task
;;                    and return HTTP 200 with updated task
;; - DELETE /tasks/:id - should delete a task with id and return HTTP 204 response.

(def app-routes
  (routes
    (context "/api" []

      (ANY "/echo" request
        (rs/ok {:echo request}))

      (GET "/tasks" []
        (rs/ok (db/fetch-tasks)))

      (POST "/tasks" request
        (rs/created (db/create-task! (:body request))))

      (GET "/tasks/:id" [id]
        (if-let [task (db/fetch-task id)]
          (rs/ok task)
          (rs/not-found)))

      (PUT "/tasks/:id" request
        (let [body (:body request)
              id (get-in request [:params :id])]
          (rs/ok (db/update-task! id body))))

      (DELETE "/tasks/:id" [id]
        (db/delete-task! id)
        (rs/no-content)))))

(def handler
  (-> app-routes
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (wrap-access-log)))
