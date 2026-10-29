package org.towerdefense.modelo.enemigo;

import org.towerdefense.modelo.Posicion;

public class EnemigoMedio extends Enemigo {
    public EnemigoMedio(Posicion pos) {
        super(pos, 6, 2, 20, 50.0, "medio", 25, 25);
    }
}
