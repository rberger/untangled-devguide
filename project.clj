(defproject untangled-tutorial "0.1.0-SNAPSHOT"
  :description "A Tutorial for the Untangled Web Framework"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.omcljs/om "1.0.0-alpha46"]
                 [com.datomic/datomic-free "0.9.5404" :exclusions [org.clojure/tools.cli]]
                 [com.google.guava/guava "19.0"]
                 [commons-codec "1.10"]
                 [commons-io "2.5"]
                 [org.clojure/core.async "0.2.391"]
                 [http-kit "2.2.0"]
                 [bidi "2.0.9"]
                 [clj-time "0.11.0"]
                 [lein-doo "0.1.7" :scope "test"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/core.cache "0.6.5"]
                 [navis/untangled-client "0.5.6"]
                 [cljsjs/d3 "3.5.7-1"]
                 [navis/untangled-server "0.6.1"]
                 [navis/untangled-spec "0.3.8" :scope "test"]
                 [navis/untangled-datomic "0.4.9"]]

  ; server source paths
  :source-paths ["src/server" "src/shared" "test/server" "test/shared" "src/tutorial"]
  :test-paths ["test/server" "test/shared"]

  :plugins [[lein-cljsbuild "1.1.4"]

            ; Run server side tests with spec output
            [com.jakemccrary/lein-test-refresh "0.15.0"]

            ; Used for running CI (command line) client tests
            [lein-doo "0.1.7" :exclusions [org.clojure/tools.reader]]

            ; Internationalization extraction/generation
            [navis/untangled-lein-i18n "0.1.2" :exclusions [org.apache.maven.wagon/wagon-provider-api org.codehaus.plexus/plexus-utils org.clojure/tools.cli]]]

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :doo {:build "automated-tests"
        :paths {:karma "node_modules/karma/bin/karma"}}

  ; Configure test-refresh to show custom spec outline
  :test-refresh {:report       untangled-spec.reporters.terminal/untangled-report
                 :changes-only true
                 :with-repl    true}

  ; i18n lein plugin config
  :untangled-i18n {:default-locale        "en-US"
                   :translation-namespace "app.i18n"
                   :source-folder         "src/tutorial"
                   :target-build          "i18n"}

  :cljsbuild {:builds [{:id           "test"
                        :figwheel     true
                        :source-paths ["src/shared" "test/client" "test/shared"]
                        :compiler     {:main                 app.suite
                                       :asset-path           "js/specs"
                                       :output-to            "resources/public/js/specs.js"
                                       :output-dir           "resources/public/js/specs"
                                       :recompile-dependents true
                                       }}
                       {:id           "automated-tests"
                        :source-paths ["test/shared" "test/client" "src/shared"]
                        :compiler     {:output-to     "resources/private/js/unit-tests.js"
                                       :main          app.all-tests
                                       :asset-path    "js"
                                       :output-dir    "resources/private/js"
                                       :optimizations :none
                                       }}
                       {:id           "tutorial"
                        :figwheel     {:devcards true}
                        :source-paths ["src/tutorial" "src/shared"]
                        :compiler     {
                                       :main                 untangled-tutorial.tutorial
                                       :source-map-timestamp true
                                       :asset-path           "js/tutorial"
                                       :output-to            "resources/public/js/tutorial.js"
                                       :output-dir           "resources/public/js/tutorial"
                                       :recompile-dependents true
                                       :verbose              false
                                       :foreign-libs         [{:provides ["cljsjs.codemirror.addons.closebrackets"]
                                                               :requires ["cljsjs.codemirror"]
                                                               :file     "resources/public/codemirror/closebrackets-min.js"}
                                                              {:provides ["cljsjs.codemirror.addons.matchbrackets"]
                                                               :requires ["cljsjs.codemirror"]
                                                               :file     "resources/public/codemirror/matchbrackets-min.js"}]}}
                       {:id           "pages"
                        :source-paths ["src/tutorial" "src/pages" "src/shared"]
                        :compiler     {
                                       :main          core
                                       :devcards      true
                                       :asset-path    "js/pages"
                                       :output-to     "resources/public/js/pages.js"
                                       :output-dir    "resources/public/js/pages"
                                       :optimizations :advanced
                                       :foreign-libs  [{:provides ["cljsjs.codemirror.addons.closebrackets"]
                                                        :requires ["cljsjs.codemirror"]
                                                        :file     "resources/public/codemirror/closebrackets-min.js"}
                                                       {:provides ["cljsjs.codemirror.addons.matchbrackets"]
                                                        :requires ["cljsjs.codemirror"]
                                                        :file     "resources/public/codemirror/matchbrackets-min.js"}]}}]}

  :profiles {
             :dev {
                   :dependencies [[devcards "0.2.1-7"]
                                  [figwheel-sidecar "0.5.7" :exclusions [ring/ring-core org.clojure/tools.analyzer.jvm org.clojure/tools.analyzer org.clojure/core.memoize org.clojure/data.priority-map]]
                                  [binaryage/devtools "0.6.1"]
                                  [cljsjs/codemirror "5.8.0-0"]]
                   :source-paths ["dev/server" "src/server" "src/shared"]
                   :repl-options {:init-ns user
                                  :port    7001}
                   }
             }
  )
