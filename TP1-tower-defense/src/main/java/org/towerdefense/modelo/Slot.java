package org.towerdefense.modelo;

import org.towerdefense.modelo.torreta.Torreta;

public final class Slot {

    private final Posicion posicion;
    private Torreta torreta;

    public Slot(Posicion posicion) {
        this.posicion = posicion;
    }

    public Posicion posicion() {
        return posicion;
    }

    public boolean estaVacio() {
        return torreta == null;
    }

    public Torreta torreta() {
        return torreta;
    }

    public void colocar(Torreta torreta) {
        this.torreta = torreta;
    }
}

