(ns workshop.exercise6
  (:require
    [workshop.utils :refer :all]
    [workshop.exercise1 :refer [pprint-request]]
    [workshop.exercise2 :refer [wrap-access-log]]
    [workshop.memdb :as db]
    [ring.util.http-response :as rs]
    [ring.middleware.params :refer [wrap-params]]
    [compojure.core :refer :all]
    [hiccup.page :as page]
    [hiccup.form :as form]))

;; Exercise goals
;; Write template for tasks page. It should include
;; - a list of tasks rendered as unordered list
;; - a form to create a new task

(defn echo-page [request]
  (page/html5
    [:head
     [:title "HTTP Echo"]]
    [:body
     [:h1 "HTTP Echo"]
     [:div#echo-content
      [:pre (pprint-request request)]]]))

(defn tasks-page []
  (implement-me!))

(defroutes app-routes
  (ANY "/echo" request
    (rs/ok (echo-page request)))

  (GET "/tasks" []
    (rs/ok (tasks-page)))

  (POST "/tasks" [task-content]
    (let [new-task {:content task-content :done false}]
      (db/create-task! new-task)
      (rs/see-other "/tasks"))))

(def handler
  (-> app-routes
      (wrap-params)
      (wrap-access-log)))
