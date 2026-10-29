package org.towerdefense.modelo;

public record Posicion(double x, double y) {

    public double distanciaA(Posicion otra) {
        return Math.hypot(x - otra.x, y - otra.y);
    }
}
