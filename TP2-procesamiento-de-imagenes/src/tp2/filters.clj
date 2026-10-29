(ns tp2.filters)

(defn invertir-pixel [pixel]
  (mapv (fn [x] (- 255 x)) pixel))

(defn brillo-pixel [pixel cantidad]
  (mapv (fn [x]
          (max 0 (min 255 (+ x cantidad)))) pixel))

(defn aplicar-efecto-por-pixel [imagen efecto]
  (vec (pmap (fn [fila]
          (mapv efecto fila))
        imagen)))


(defn invertir [imagen]
  (aplicar-efecto-por-pixel imagen invertir-pixel))

(defn brillo [imagen cantidad]
  (aplicar-efecto-por-pixel imagen #(brillo-pixel % cantidad)))


(def kernel-gaussiano
  [[-1 -1 1]  [0 -1 2]  [1 -1 1]
   [-1  0 2]  [0  0 4]  [1  0 2]
   [-1  1 1]  [0  1 2]  [1  1 1]])

(defn obtener-pixel [imagen ancho alto x y]
  (let [max-y (- alto 1)
        max-x (- ancho 1)
        y-imagen (max 0 (min y max-y))
        x-imagen (max 0 (min x max-x))]
    (get-in imagen [y-imagen x-imagen])))

(defn sumar-dos-pixeles [pixel1 pixel2]
  (mapv + pixel1 pixel2))

(defn multiplicar-peso [pixel peso]
  (mapv #(* % peso) pixel))

(defn promediar-pixel [imagen ancho alto x y]
  (let [lista-pixeles (map #(let [[dx dy peso] %
                                  pixel-rgb (obtener-pixel imagen ancho alto (+ x dx) (+ y dy))]
                              (multiplicar-peso pixel-rgb peso))
                           kernel-gaussiano)
        pixel-sumado (reduce sumar-dos-pixeles lista-pixeles)]
    (mapv #(quot % 16) pixel-sumado)))

(defn difuminado [imagen]
  (let [alto (count imagen)
        ancho (count (first imagen))]
    (vec (pmap (fn [y]
                 (mapv (fn [x]
                         (promediar-pixel imagen ancho alto x y))
                       (range ancho)))
               (range alto)))))