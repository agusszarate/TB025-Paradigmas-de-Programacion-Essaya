package org.towerdefense.vista;

import javafx.application.Platform;
import org.towerdefense.modelo.evento.Evento;
import org.towerdefense.modelo.evento.EventoListener;
import org.towerdefense.modelo.evento.NivelGanado;
import org.towerdefense.modelo.evento.NivelPerdido;

public final class OrquestadorNiveles implements EventoListener {

    private final Main main;
    private final int totalNiveles;

    public OrquestadorNiveles(Main main, int totalNiveles) {
        this.main = main;
        this.totalNiveles = totalNiveles;
    }

    @Override
    public void notificar(Evento evento) {
        evento.despachar(this);
    }

    @Override
    public void cuandoNivelSeGana(NivelGanado evento) {
        int nivel = evento.nivel();
        Platform.runLater(() -> {
            if (nivel < totalNiveles) {
                main.mostrarVictoriaNivel(nivel);
            } else {
                main.mostrarVictoriaDefinitiva();
            }
        });
    }

    @Override
    public void cuandoNivelSePierde(NivelPerdido evento) {
        int nivel = evento.nivel();
        Platform.runLater(() -> main.mostrarDerrota(nivel));
    }
}
