package org.towerdefense.vista.pantalla;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import org.towerdefense.modelo.Partida;
import org.towerdefense.vista.controlador.JuegoControlador;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class PantallaJuego {

    private static final String FXML_PATH = "/fxml/juego.fxml";
    private static final String CSS_PATH = "/styles.css";

    private final Scene scene;
    private final JuegoControlador controlador;

    public PantallaJuego(Partida partida) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        controlador = new JuegoControlador(partida);
        loader.setController(controlador);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + FXML_PATH, e);
        }
        this.scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
    }

    public Scene scene() {
        return scene;
    }

    public Canvas canvas() {
        return controlador.canvas();
    }

    public void actualizarHud() {
        controlador.actualizar();
    }
}
