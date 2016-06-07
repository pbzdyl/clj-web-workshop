(ns workshop.exercise4
  (:require
    [clj-http.client :as http]
    [workshop.utils :refer :all]))

;; Exercise goals
;; shorten-url! function should use Google URL Shortener API to shorten the provided long-url.
;; The function should return just the short URL, not the whole Google URL Shortener response.

;; https://developers.google.com/url-shortener/v1/getting_started
;; https://github.com/dakrone/clj-http
;;
;; curl https://www.googleapis.com/urlshortener/v1/url?key=your-api-key -H 'Content-Type: application/json' -d '{"longUrl": "http://www.google.com/"}'

(def api-key "AIzaSyD8Pmu_34AgApBYaxW0LRFe7-wX7qKAnTw")

(defn shorten-url! [long-url]
  (-> (http/post "https://www.googleapis.com/urlshortener/v1/url"
                 {:form-params  {:longUrl long-url}
                  :content-type :json
                  :query-params {"key" api-key}
                  :as :json})
      :body
      :id))
