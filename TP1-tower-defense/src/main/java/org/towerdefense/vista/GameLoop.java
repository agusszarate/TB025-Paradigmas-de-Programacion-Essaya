package org.towerdefense.vista;

import javafx.animation.AnimationTimer;
import org.towerdefense.modelo.Partida;

public final class GameLoop extends AnimationTimer {

    private final Partida partida;
    private final Runnable alRenderizar;
    private long instanteAnterior = -1;

    public GameLoop(Partida partida, Runnable alRenderizar) {
        this.partida = partida;
        this.alRenderizar = alRenderizar;
    }

    @Override
    public void handle(long ahora) {
        if (instanteAnterior < 0) {
            instanteAnterior = ahora;
            return;
        }
        double dt = (ahora - instanteAnterior) / 1e9;
        instanteAnterior = ahora;

        partida.tick(dt);
        alRenderizar.run();

        if (partida.terminada()) {
            stop();
        }
    }
}
