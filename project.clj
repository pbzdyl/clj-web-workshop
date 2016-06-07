(defproject workshop "0.1.0-SNAPSHOT"

  :description "Clojure Web App Workshop"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.5.0-RC1"]
                 [ring/ring-jetty-adapter "1.5.0-RC1"]
                 [ring/ring-json "0.4.0"]
                 [metosin/ring-http-response "0.7.0"]
                 [metosin/compojure-api "1.1.2"]
                 [liberator "0.14.1"]
                 [hiccup "1.0.5"]
                 [kerodon "0.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.13"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [clj-http "3.1.0"]
                 [clj-http-fake "1.0.2"]
                 [yesql "0.5.3"]
                 [com.h2database/h2 "1.4.192"]
                 [migratus "0.8.25"]
                 [ring/ring-mock "0.3.0"]
                 [org.clojars.runa/conjure "2.2.0"]]

  :plugins [[lein-ring "0.9.7"]
            [migratus-lein "0.3.7"]]

  :ring {:handler workshop.demo2/handler}

  :migratus {:store :database
             :migration-dir "migrations"}

  :profiles {:dev {:plugins [[quickie "0.4.2"]]
                   :dependencies [[pjstadig/humane-test-output "0.8.0"]]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]}}

  :aliases {"autotest" ["quickie"]})
