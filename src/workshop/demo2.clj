(ns workshop.demo2
  (:require
    [workshop.utils :refer :all]
    [workshop.demo2.utils :refer :all]
    [workshop.memdb :as db]
    [workshop.exercise4 :as shortener]
    [compojure.core :refer :all]
    [liberator.core :refer [resource defresource]]
    [liberator.dev :refer [wrap-trace]]))

;; https://clojure-liberator.github.io/liberator/tutorial/decision-graph.html
;; https://clojure-liberator.github.io/liberator/tutorial/debugging.html
;; http://localhost:3000/x-liberator/requests/

(defroutes app-routes
  (ANY "/api/tasks" []
    (resource
      :available-media-types ["application/json"]
      :allowed-methods [:get :post]
      :known-content-type? #(check-content-type % ["application/json"])
      :malformed? #(parse-json % ::data)
      :post! #(let [new-task (db/create-task! (::data %))
                    id (:id new-task)]
               {::id id})
      :post-redirect? true
      :location #(build-task-url (get % :request) (get % ::id))
      :handle-ok (fn [_] (db/fetch-tasks))))

  (ANY "/api/tasks/:id" [id]
    (resource
      :allowed-methods [:get :put :delete]
      :known-content-type? #(check-content-type % ["application/json"])
      :exists? (fn [_]
                 (some? (db/fetch-task id)))
      :existed? (fn [_]
                  (or (db/fetch-task id) ::sentinel))
      :available-media-types ["application/json"]
      :handle-ok ::entity
      :delete! (fn [_] (db/delete-task! id))
      :malformed? #(parse-json % ::data)
      :can-put-to-missing? false
      :respond-with-entity? true
      :put! (fn [ctx]
              {::entity (db/update-task! id (::data ctx))})
      :new? (fn [_] (println "new?") (nil? (or (db/fetch-task id) ::sentinel)))))

  (ANY "/api/tasks/:id/completion" [id]
    (resource
      :allowed-methods [:put]
      :available-media-types ["application/json"]
      :known-content-type? #(check-content-type % ["application/json"])
      :exists? (fn [_]
                 (some? (db/fetch-task id)))
      :malformed? #(parse-json % ::data)
      :can-put-to-missing? false
      :put! (fn [ctx]
              {::entity (db/set-task-done-status! id (-> ctx :request ::data :done))})
      :handle-ok ::entity))

  (ANY "/api/tasks/:id/short-url" [id]
    (resource
      :allowed-methods [:post]
      :available-media-types ["application/json"]
      :exists? (fn [_]
                 (some? (db/fetch-task id)))
      :new? false
      :respond-with-entity? true
      :post! #(let [full-url (build-task-url (:request %) id)
                    short-url (shortener/shorten-url! full-url)]
               {::entity {:fullUrl  full-url
                          :shortUrl short-url}})
      :handle-ok ::entity)))

(def handler
  (-> app-routes
      (wrap-trace :header :ui)))
