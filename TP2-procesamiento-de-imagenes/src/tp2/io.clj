(ns tp2.io
  (:import [javax.imageio ImageIO]
           [java.io File]
           [java.awt.image BufferedImage]))

(defn cargar [ruta]
  (let [bi (ImageIO/read (File. ^String ruta))
        w (.getWidth bi)
        h (.getHeight bi)]
    (vec (for [y (range h)]
           (vec (for [x (range w)]
                  (let [argb (.getRGB bi x y)]
                    [(bit-and (bit-shift-right argb 16) 0xff)
                     (bit-and (bit-shift-right argb 8) 0xff)
                     (bit-and argb 0xff)])))))))

(defn guardar [imagen ruta formato]
  (let [h (count imagen)
        w (count (first imagen))
        bi (BufferedImage. w h BufferedImage/TYPE_INT_RGB)]
    (doseq [[y fila] (map-indexed vector imagen)
            [x [r g b]] (map-indexed vector fila)]
      (.setRGB bi x y (int (bit-or (bit-shift-left (int r) 16)
                                   (bit-shift-left (int g) 8)
                                   (int b)))))
    (ImageIO/write bi formato (File. ^String ruta))))
