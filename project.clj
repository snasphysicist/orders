(defproject orders "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
    [org.clojure/clojure "1.11.1"]
    [com.github.seancorfield/next.jdbc "1.3.883"]
    [org.postgresql/postgresql "42.2.10"]
    [org.clojure/data.json "2.4.0"]
    [compojure "1.7.0"]
    [http-kit "2.3.0"]
    [clj-http "3.12.3"]
    [clj-time "0.6.0"]
    [org.clojure/tools.logging "1.2.4"]]
  :main ^:skip-aot orders.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
