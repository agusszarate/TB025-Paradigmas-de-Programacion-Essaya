package org.towerdefense.vista.controlador;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.towerdefense.modelo.Partida;
import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.Slot;

import java.util.Optional;

public final class JuegoControlador {

    private static final double RADIO_CLICK_SLOT = 30;
    private static final String SIN_SELECCION = "ninguna";

    private final Partida partida;

    @FXML private Label lblNivel;
    @FXML private Label lblDinero;
    @FXML private Label lblPuntaje;
    @FXML private Label lblVidaBase;
    @FXML private Label lblSeleccion;
    @FXML private Canvas canvas;

    private final IntegerProperty dineroProperty = new SimpleIntegerProperty();
    private final IntegerProperty puntajeProperty = new SimpleIntegerProperty();
    private final IntegerProperty vidaBaseProperty = new SimpleIntegerProperty();
    private final IntegerProperty nivelProperty = new SimpleIntegerProperty();
    private final StringProperty seleccionProperty = new SimpleStringProperty(SIN_SELECCION);

    private String tipoSeleccionado;

    public JuegoControlador(Partida partida) {
        this.partida = partida;
    }

    @FXML
    private void initialize() {
        nivelProperty.set(partida.numero());
        dineroProperty.set(partida.dinero());
        puntajeProperty.set(partida.puntaje());
        vidaBaseProperty.set(partida.vidaBase());

        lblNivel.textProperty().bind(nivelProperty.asString("Nivel %d"));
        lblDinero.textProperty().bind(dineroProperty.asString("Dinero: %d"));
        lblPuntaje.textProperty().bind(puntajeProperty.asString("Puntaje: %d"));
        lblVidaBase.textProperty().bind(vidaBaseProperty.asString("Base: %d"));
        lblSeleccion.textProperty().bind(Bindings.concat("Seleccionada: ", seleccionProperty));
    }

    public Canvas canvas() {
        return canvas;
    }

    public void actualizar() {
        dineroProperty.set(partida.dinero());
        puntajeProperty.set(partida.puntaje());
        vidaBaseProperty.set(partida.vidaBase());
    }

    @FXML
    private void seleccionarSimple() {
        seleccionar("simple", "Simple");
    }

    @FXML
    private void seleccionarRapida() {
        seleccionar("rapida", "Rápida");
    }

    @FXML
    private void seleccionarPotente() {
        seleccionar("potente", "Potente");
    }

    private void seleccionar(String tipo, String etiqueta) {
        this.tipoSeleccionado = tipo;
        this.seleccionProperty.set(etiqueta);
    }

    @FXML
    private void manejarClick(MouseEvent evento) {
        if (tipoSeleccionado == null) {
            return;
        }
        Posicion click = new Posicion(evento.getX(), evento.getY());
        Optional<Slot> elegido = partida.slots().stream()
                .filter(s -> s.posicion().distanciaA(click) <= RADIO_CLICK_SLOT)
                .findFirst();
        if (elegido.isEmpty()) {
            return;
        }
        partida.instalarTorreta(elegido.get(), tipoSeleccionado);
        tipoSeleccionado = null;
        seleccionProperty.set(SIN_SELECCION);
    }
}
