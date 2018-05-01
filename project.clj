(defproject planmill-sum "1.0"
  :description "Calculate total sum for a Planmill hour report"
  :license {:name "MIT"
            :url "https://github.com/ykarikos/planmill-sum/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [dk.ative/docjure "1.12.0"]
                 [org.clojure/data.xml "0.0.8"]]
  :main ^:skip-aot planmill-sum.core
  :uberjar-name "planmill-sum.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
