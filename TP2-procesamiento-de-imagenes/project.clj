(defproject tp2 "0.1.0-SNAPSHOT"
  :description "Procesador de imágenes con pipeline de filtros"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [org.openjfx/javafx-base "25.0.1" :classifier "mac-aarch64"]
                 [org.openjfx/javafx-graphics "25.0.1" :classifier "mac-aarch64"]
                 [org.openjfx/javafx-controls "25.0.1" :classifier "mac-aarch64"]
                 [org.openjfx/javafx-base "25.0.1" :classifier "win"]
                 [org.openjfx/javafx-graphics "25.0.1" :classifier "win"]
                 [org.openjfx/javafx-controls "25.0.1" :classifier "win"]]

  :java-source-paths ["src"]
  :main tp2.core
  :jvm-opts ["--enable-native-access=ALL-UNNAMED"]
  :aot [tp2.core]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
