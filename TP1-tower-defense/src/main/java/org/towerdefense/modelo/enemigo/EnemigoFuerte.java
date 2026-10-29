package org.towerdefense.modelo.enemigo;

import org.towerdefense.modelo.Posicion;

public class EnemigoFuerte extends Enemigo {
    public EnemigoFuerte(Posicion pos) {
        super(pos, 15, 3, 30, 50.0, "fuerte", 25, 25);
    }
}
