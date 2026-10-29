package org.towerdefense.vista.controlador;

import javafx.fxml.FXML;
import org.towerdefense.vista.Main;

public final class MenuControlador {

    private final Main main;

    public MenuControlador(Main main) {
        this.main = main;
    }

    @FXML
    private void alIniciar() {
        main.iniciarNivel(1);
    }

    @FXML
    private void alSalir() {
        main.salir();
    }
}
