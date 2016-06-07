(ns workshop.exercise2
  (:require
    [workshop.utils :refer :all]
    [workshop.exercise1 :as exercise1]
    [clojure.pprint :refer [pprint]]
    [clojure.tools.logging :as log]))

;; Exercise goal
;; Write access logging middleware. It should log using clojure.tools.logging/log.
;; Each request should be logged twice:
;; 1. before the request is passed to the wrapped handler: log request method and request URI
;; 2. after the response has been produced by the wrapped handler: log request method, request URI, response status code
;;    and wrapped handler processing time

(defn wrap-access-log
  [handler]
  (implement-me!))

(def handler
  (-> exercise1/handler
      (wrap-access-log)))
