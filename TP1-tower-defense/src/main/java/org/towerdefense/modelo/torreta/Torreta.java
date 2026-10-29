package org.towerdefense.modelo.torreta;
import org.towerdefense.modelo.Color;
import org.towerdefense.modelo.ConsultaDeEnemigos;
import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.enemigo.Enemigo;
import org.towerdefense.modelo.proyectil.Proyectil;

public abstract class Torreta {

    private static final double VELOCIDAD_PROYECTIL = 400.0;

    private final Posicion posicion;
    private final int rango;
    private final double cadenciaSegundos;
    private final int danio;
    private final Color colorProyectil;
    private final String tipo;
    private final int costo;
    private double tiempoDesdeUltimoDisparo;

    protected Torreta(Posicion posicion, int rango, double cadenciaSegundos, int danio,
                      Color colorProyectil, String tipo, int costo) {
        this.posicion = posicion;
        this.rango = rango;
        this.cadenciaSegundos = cadenciaSegundos;
        this.danio = danio;
        this.colorProyectil = colorProyectil;
        this.tipo = tipo;
        this.costo = costo;
        this.tiempoDesdeUltimoDisparo = 0.0;
    }

    public Proyectil actualizar(double dt, ConsultaDeEnemigos consulta) {
        tiempoDesdeUltimoDisparo += dt;
        if (tiempoDesdeUltimoDisparo < cadenciaSegundos) {
            return null;
        }

        Enemigo blanco = consulta.primeroEnRuta(this.posicion, this.rango);
        if (blanco == null) {
            return null;
        }

        tiempoDesdeUltimoDisparo = 0.0;
        return new Proyectil(this.posicion, blanco, this.danio, VELOCIDAD_PROYECTIL, this.colorProyectil);
    }

    public Posicion posicion() {
        return posicion;
    }

    public int rango() {
        return rango;
    }

    public int costo() {
        return costo;
    }

    public String tipo() {
        return tipo;
    }
}
