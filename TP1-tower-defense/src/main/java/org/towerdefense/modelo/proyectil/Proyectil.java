package org.towerdefense.modelo.proyectil;

import org.towerdefense.modelo.Color;
import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.enemigo.Enemigo;

public class Proyectil {

    private static final double ANCHO_AREA = 800.0;
    private static final double ALTO_AREA = 600.0;

    private Posicion posicion;
    private final Enemigo blanco;
    private final int danio;
    private final double velocidad;
    private final Color color;
    private boolean activo;

    public Proyectil(Posicion posicionInicial, Enemigo blanco, int danio, double velocidad, Color color) {
        this.posicion = posicionInicial;
        this.blanco = blanco;
        this.danio = danio;
        this.velocidad = velocidad;
        this.color = color;
        this.activo = true;
    }

    public void avanzar(double dt) {
        if (!activo || !blanco.estaVivo()) {
            this.activo = false;
            return;
        }

        Posicion destino = blanco.posicion();
        double dx = destino.x() - posicion.x();
        double dy = destino.y() - posicion.y();
        double distancia = Math.sqrt(dx * dx + dy * dy);
        if (distancia == 0.0) {
            return;
        }

        double paso = velocidad * dt;
        if (paso >= distancia) {
            posicion = destino;
        } else {
            double ux = dx / distancia;
            double uy = dy / distancia;
            posicion = new Posicion(posicion.x() + ux * paso, posicion.y() + uy * paso);
        }

        if (blanco.contieneA(posicion)) {
            aplicarDanio();
            this.activo = false;
        }

        if (fueraDelArea()) {
            this.activo = false;
        }
    }

    public boolean fueraDelArea() {
        return posicion.x() < 0 || posicion.x() > ANCHO_AREA || posicion.y() < 0 || posicion.y() > ALTO_AREA;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void aplicarDanio() {
        blanco.recibirDanio(danio);
    }

    public Posicion posicion() {
        return posicion;
    }

    public Color color() {
        return color;
    }

    public Enemigo blanco() {
        return blanco;
    }
}
