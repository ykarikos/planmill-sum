(defproject planmill-sum "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [dk.ative/docjure "1.12.0"]]
  :main ^:skip-aot planmill-sum.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})