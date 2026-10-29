(ns tp2.core
  (:import [javafx.application Application])
  (:gen-class))

(defn -main
  [& args]
  (Application/launch (Class/forName "tp2.ui.Ventana")
                      (into-array String args)))
