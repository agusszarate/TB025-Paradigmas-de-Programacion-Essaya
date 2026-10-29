package org.towerdefense.vista.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public final class CartelControlador {

    private final String titulo;
    private final String mensaje;
    private final String textoBoton;
    private final Runnable accion;

    @FXML private Label lblTitulo;
    @FXML private Label lblMensaje;
    @FXML private Button botonContinuar;

    public CartelControlador(String titulo, String mensaje, String textoBoton, Runnable accion) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.textoBoton = textoBoton;
        this.accion = accion;
    }

    @FXML
    private void initialize() {
        lblTitulo.setText(titulo);
        lblMensaje.setText(mensaje);
        botonContinuar.setText(textoBoton);
    }

    @FXML
    private void alContinuar() {
        accion.run();
    }
}
