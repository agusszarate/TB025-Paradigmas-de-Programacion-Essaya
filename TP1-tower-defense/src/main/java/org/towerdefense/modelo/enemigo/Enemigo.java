package org.towerdefense.modelo.enemigo;

import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.Ruta;

public abstract class Enemigo {
    private Posicion posicion;
    private int vida;
    private final int vidaMaxima;
    private final int danio;
    private final int recompensa;
    private double velocidad;
    private double progreso;
    private final String tipo;
    private final int ancho;
    private final int alto;

    public Enemigo(Posicion pos, int vida, int danio, int recompensa, double velocidad, String tipo, int ancho, int alto) {
        this.posicion = pos;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.danio = danio;
        this.recompensa = recompensa;
        this.velocidad = velocidad;
        this.progreso = 0.0;
        this.tipo = tipo;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void avanzar(double dt, Ruta ruta) {
        progreso += velocidad * dt;
        posicion = ruta.puntoEn(progreso);
    }

    public boolean contieneA(Posicion p) {
        int mitad_ancho = ancho/2;
        int mitad_alto = alto/2;
        return p.x() >= this.posicion.x() - mitad_ancho && p.x() <= this.posicion.x() + mitad_ancho && p.y() >= this.posicion.y() - mitad_alto && p.y() <= this.posicion.y() + mitad_alto;
    }

    public int danioABase() {
        return danio;
    }

    public int recompensa() {
        return recompensa;
    }

    public void recibirDanio(int danio) {
        this.vida -= danio;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public Posicion posicion() {
        return this.posicion;
    }

    public double progreso() {
        return this.progreso;
    }

    public String tipo() {
        return this.tipo;
    }

    public int vida() {
        return this.vida;
    }

    public int vidaMaxima() {
        return this.vidaMaxima;
    }
}
