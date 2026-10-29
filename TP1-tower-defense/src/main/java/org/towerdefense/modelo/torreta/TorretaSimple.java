package org.towerdefense.modelo.torreta;

import org.towerdefense.modelo.Color;
import org.towerdefense.modelo.Posicion;

public final class TorretaSimple extends Torreta {

    public TorretaSimple(Posicion posicion) {
        super(posicion, 100, 1.0, 1, Color.AMARILLO, "simple", 50);
    }
}
