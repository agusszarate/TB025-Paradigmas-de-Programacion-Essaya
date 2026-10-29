(ns tp2.pipeline)

(defn pipeline-vacio []
  [])

(defn agregar [pipeline entrada]
  (conj pipeline entrada))

(defn quitar [pipeline idx]
  (vec (concat (subvec pipeline 0 idx)
               (subvec pipeline (inc idx)))))

(defn limpiar [_pipeline]
  [])

(defn aplicar [imagen pipeline]
  (reduce (fn [img entrada] ((:f entrada) img))
          imagen
          pipeline))

(defn aplicar-async [imagen pipeline on-done]
  (future
    (let [resultado (aplicar imagen pipeline)]
      (on-done resultado)
      resultado)))
