(ns workshop.exercise1
  (:require
    [workshop.utils :refer [implement-me!]]
    [ring.util.response :as rs]
    [clojure.pprint :refer [pprint]]))

;; Exercise goal
;; Write a HTTP echo handler function that will handle all requests and response with HTTP 200 response
;; with request's string representation as its body
;; (for example {:status 200 :body "... request converted to string ..."})

(defn pprint-request [request]
  (with-out-str (pprint request)))

(defn handler
  [request]
  (-> request
      (pprint-request)
      (rs/response)))
