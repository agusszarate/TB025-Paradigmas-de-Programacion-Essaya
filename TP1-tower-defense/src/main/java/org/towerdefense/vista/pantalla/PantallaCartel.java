package org.towerdefense.vista.pantalla;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.towerdefense.vista.controlador.CartelControlador;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class PantallaCartel {

    private static final String FXML_PATH = "/fxml/cartel.fxml";
    private static final String CSS_PATH = "/styles.css";
    private static final int ANCHO = 800;
    private static final int ALTO = 600;

    private final Scene scene;

    public PantallaCartel(String titulo, String mensaje, String textoBoton, Runnable alContinuar) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        loader.setController(new CartelControlador(titulo, mensaje, textoBoton, alContinuar));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + FXML_PATH, e);
        }
        this.scene = new Scene(root, ANCHO, ALTO);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
    }

    public Scene scene() {
        return scene;
    }
}
