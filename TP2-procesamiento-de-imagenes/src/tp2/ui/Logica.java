package tp2.ui;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.function.Consumer;

public final class Logica {

    private Logica() {
    }

    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("tp2.controlador"));
    }

    private static IFn fn(String nombre) {
        return Clojure.var("tp2.controlador", nombre);
    }

    public static String[] filtros() {
        return (String[]) fn("nombres-filtros").invoke();
    }

    public static Object cargar(String ruta) {
        return fn("cargar").invoke(ruta);
    }

    public static void guardar(Object imagen, String ruta, String formato) {
        fn("guardar").invoke(imagen, ruta, formato);
    }

    public static Object pipelineVacio() {
        return fn("pipeline-vacio").invoke();
    }

    public static Object agregar(Object pipeline, String nombreFiltro) {
        return fn("agregar").invoke(pipeline, nombreFiltro);
    }

    public static Object quitar(Object pipeline, int indice) {
        return fn("quitar").invoke(pipeline, indice);
    }

    public static Object limpiar(Object pipeline) {
        return fn("limpiar").invoke(pipeline);
    }

    public static void aplicarAsync(Object imagen, Object pipeline, Consumer<Object> onDone) {
        fn("aplicar-async").invoke(imagen, pipeline, onDone);
    }

    public static Object aImagenFx(Object imagen) {
        return fn("->fx-image").invoke(imagen);
    }
}
