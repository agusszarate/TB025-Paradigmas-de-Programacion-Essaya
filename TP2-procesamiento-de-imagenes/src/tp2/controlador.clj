(ns tp2.controlador
  (:require [tp2.pipeline :as p]
            [tp2.filters :as f]
            [tp2.io :as io])
  (:import [javafx.scene.image WritableImage]
           [java.util.function Consumer]))

(def filtros
  [{:nombre "Invertir"   :f f/invertir}
   {:nombre "Brillo +40" :f #(f/brillo % 40)}
   {:nombre "Brillo -40" :f #(f/brillo % -40)}
   {:nombre "Difuminado" :f f/difuminado}])

(defn nombres-filtros []
  (into-array String (mapv :nombre filtros)))

(defn- entrada [nombre]
  (some #(when (= nombre (:nombre %)) %) filtros))

(defn pipeline-vacio [] (p/pipeline-vacio))
(defn agregar [pipeline nombre] (p/agregar pipeline (entrada nombre)))
(defn quitar [pipeline idx] (p/quitar pipeline (int idx)))
(defn limpiar [pipeline] (p/limpiar pipeline))

(defn cargar [ruta] (io/cargar ruta))
(defn guardar [imagen ruta formato] (io/guardar imagen ruta formato))

(defn aplicar-async [imagen pipeline ^Consumer on-done]
  (p/aplicar-async imagen pipeline (fn [res] (.accept on-done res))))

(defn ->fx-image [imagen]
  (let [h (count imagen)
        w (count (first imagen))
        wi (WritableImage. w h)
        pw (.getPixelWriter wi)]
    (doseq [[y fila] (map-indexed vector imagen)
            [x [r g b]] (map-indexed vector fila)]
      (.setArgb pw x y (unchecked-int (bit-or 0xff000000
                                              (bit-shift-left (int r) 16)
                                              (bit-shift-left (int g) 8)
                                              (int b)))))
    wi))
