package org.towerdefense.modelo.enemigo;

import org.towerdefense.modelo.Posicion;

public class EnemigoDebil extends Enemigo {
    public EnemigoDebil(Posicion pos) {
        super(pos, 3, 1, 10, 50.0, "debil", 25, 25);
    }
}
