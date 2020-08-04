(defproject org.pinkgorilla/gorilla-plot "1.2.5"
  :description "A simple data-driven plotting dsl using Vega via Gorilla UI."
  :url "https://github.com/pink-gorilla/gorilla-plot"
  :license {:name "MIT"}

  :min-lein-version "2.9.3"
  :min-java-version "1.11"

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

    ;; TODO: prep tasks breaks alias???
  ;; :prep-tasks ["build-shadow-ci"]

  :source-paths ["src"]
  :test-paths ["test"]
  :target-path  "target/jar"

  :managed-dependencies [[com.google.javascript/closure-compiler-unshaded "v20200719"]
                         [com.cognitect/transit-cljs "0.8.264"]
                         [com.fasterxml.jackson.core/jackson-core "2.11.2"]
                         [org.apache.httpcomponents/httpcore "4.4.13"]
                         [org.apache.httpcomponents/httpasyncclient "4.1.4"]
                         [com.google.code.findbugs/jsr305 "3.0.2"]
                         [org.ow2.asm/asm "8.0.1"]
                         [commons-codec "1.14"]
                         ]


  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-time "0.15.2"] ;time axis creation 
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.pinkgorilla/gorilla-ui "0.2.30"]]

  :pinkgorilla {:timbre-loglevel :info
                :backend {:explorer {:exclude #{".svn" ".git"}, :roots {:app "./notebooks"}}}
                :frontend {:explorer {:repositories [{:name "local", :url "/api/explorer", :save true}]}}
                :title "GorillaPlot notebooks"}

  :profiles {:ci  {:target    :karma
                   :output-to "target/ci.js"}

             :perf {:source-paths ["dev"]
                    :dependencies [[metasoarous/darkstar "0.1.0"]
                                   [cheshire "5.10.0"]
                                   [com.taoensso/tufte "2.1.0"]]}

             :dev {:dependencies [[org.pinkgorilla/webly "0.0.25"] ; brings shadow
                                  [clj-kondo "2020.07.29"]]

                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-shell "0.5.0"]
                                  [lein-ancient "0.6.15"]
                                  [min-java-version "0.1.0"]
                                  [org.pinkgorilla/lein-pinkgorilla "0.0.20"]]
                   :aliases      {"clj-kondo"
                                  ["run" "-m" "clj-kondo.main"]

                                  "bump-version"
                                  ["change" "version" "leiningen.release/bump-version"]

                                  "lint"            ^{:doc "Lint for dummies"}
                                  ["clj-kondo" "--lint" "src"]

                                  "build-shadow-ci" ^{:doc "Build shadow-cljs ci"}
                                  ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]

                                  "test-run"         ^{:doc "Test compiled JavaScript."}
                                  ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]

                                  "test-js"          ^{:doc "Compile & Run JavaScript."}
                                  ["do" "build-shadow-ci" ["test-run"]]

                                  "perf"              ^{:doc "performance tests"}
                                  ["with-profile" "perf" "run" "-m" "perf.core"]

                                   ;; APP

                                  "build-dev"  ^{:doc "compiles bundle-dev"}
                                  ["with-profile" "+dev" "run" "-m" "webly.build-cli" "compile" "+dev" "goldly.app/handler" "demo.app"]

                                  "build-prod"  ^{:doc "compiles bundle-prod"}
                                  ["with-profile" "+dev" "run" "-m" "webly.build-cli" "release" "+dev" "goldly.app/handler" "demo.app"]}
                   :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                  }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                   :cljfmt       {:indents {as->                [[:inner 0]]
                                            with-debug-bindings [[:inner 0]]
                                            merge-meta          [[:inner 0]]
                                            try-if-let          [[:block 1]]}}}})
