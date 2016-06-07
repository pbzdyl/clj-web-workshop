(ns workshop.demo1
  (:require
    [workshop.utils :refer :all]
    [workshop.exercise4 :as shortener]
    [workshop.exercise5 :as db]
    [ring.util.http-response :as rs]
    [compojure.api.sweet :refer :all]
    [schema.core :as s]))

(s/defschema TaskContent
  {:content s/Str
   :done    s/Bool})

(s/defschema Task
  {:id      s/Str
   :content s/Str
   :done    s/Bool})

(s/defschema TaskCompletion
  {:done s/Bool})

(s/defschema TaskShortUrl
  {:fullUrl s/Str
   :shortUrl s/Str})

(def handler
  (api
    {:swagger
     {:ui   "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title       "CLJ Web Workshop API"
                    :description "Simple REST API"}
             :tags [{:name "API", :description "Demo API"}]}}}

    (context "/api" []
      :tags ["API"]

      (GET "/tasks" []
        :return [Task]
        :summary "Returns all tasks"

        (rs/ok (db/fetch-tasks)))

      (POST "/tasks" []
        :return Task
        :body [task TaskContent]
        :summary "Creates a new task"

        (rs/created (db/create-task! task)))

      (GET "/tasks/:id" []
        :return Task
        :path-params [id :- s/Str]
        :summary "Returns a task by ID"

        (if-let [task (db/fetch-task id)]
          (rs/ok task)
          (rs/not-found)))

      (PUT "/tasks/:id" []
        :return Task
        :path-params [id :- s/Str]
        :body [task TaskContent]
        :summary "Updates a task with specified ID"

        (rs/ok (db/update-task! id task)))

      (DELETE "/tasks/:id" []
        :path-params [id :- s/Str]
        :summary "Deletes a task with specified ID"

        (db/delete-task! id)
        (rs/no-content))

      (PUT "/tasks/:id/completion" []
        :return Task
        :path-params [id :- s/Str]
        :body [completion TaskCompletion]
        :summary "Sets tasks done status to the provided value"

        (rs/ok (db/set-task-done-status! id (:done completion))))

      (POST "/tasks/:id/short-url" request
        :return TaskShortUrl
        :path-params [id :- s/Str]
        :summary "Creates a short URL to a task with provided ID"

        (let [full-url (build-task-url  request id)
              short-url (shortener/shorten-url! full-url)]
          (rs/ok {:fullUrl full-url
                  :shortUrl short-url}))))))
