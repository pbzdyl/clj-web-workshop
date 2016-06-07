(ns workshop.exercise4-test
  (:require
    [clojure.test :refer :all]
    [clj-http.fake :refer [with-global-fake-routes-in-isolation]]
    [cheshire.core :as json]
    [workshop.utils :refer :all]
    [workshop.exercise4 :refer :all]))

(deftest url-shortening
  (with-global-fake-routes-in-isolation
    {{:address      "https://www.googleapis.com/urlshortener/v1/url"
      :query-params {:key api-key}}
     {:post (constantly {:status 200
                         :body (json/generate-string {:kind "urlshortener#url"
                                                      :id "https://u.r/l"
                                                      :longUrl "http://localhost:3000/original-url"})})}}

    (is (= (shorten-url! "http://localhost:3000/original-url")
           "https://u.r/l"))))
