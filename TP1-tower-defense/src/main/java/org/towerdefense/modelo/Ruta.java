package org.towerdefense.modelo;

import java.util.ArrayList;
import java.util.List;

public final class Ruta {

    private final List<Posicion> puntos;
    private final List<Double> longitudesAcumuladas;
    private final double longitudTotal;

    public Ruta(List<Posicion> puntos) {
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("Una ruta requiere al menos 2 puntos");
        }
        this.puntos = List.copyOf(puntos);
        this.longitudesAcumuladas = new ArrayList<>(this.puntos.size());
        this.longitudesAcumuladas.add(0.0);
        double acumulado = 0.0;
        for (int i = 1; i < this.puntos.size(); i++) {
            acumulado += this.puntos.get(i - 1).distanciaA(this.puntos.get(i));
            this.longitudesAcumuladas.add(acumulado);
        }
        this.longitudTotal = acumulado;
    }

    public Posicion puntoEn(double progreso) {
        if (progreso <= 0.0) {
            return puntos.get(0);
        }
        if (progreso >= longitudTotal) {
            return puntos.get(puntos.size() - 1);
        }
        for (int i = 1; i < puntos.size(); i++) {
            double finSegmento = longitudesAcumuladas.get(i);
            if (progreso <= finSegmento) {
                double inicioSegmento = longitudesAcumuladas.get(i - 1);
                double largoSegmento = finSegmento - inicioSegmento;
                double t = (progreso - inicioSegmento) / largoSegmento;
                Posicion a = puntos.get(i - 1);
                Posicion b = puntos.get(i);
                return new Posicion(
                        a.x() + (b.x() - a.x()) * t,
                        a.y() + (b.y() - a.y()) * t);
            }
        }
        return puntos.get(puntos.size() - 1);
    }

    public double longitudTotal() {
        return longitudTotal;
    }

    public List<Posicion> puntos() {
        return puntos;
    }
}