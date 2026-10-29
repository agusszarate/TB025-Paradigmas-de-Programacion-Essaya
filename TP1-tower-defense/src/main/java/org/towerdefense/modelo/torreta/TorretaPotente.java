package org.towerdefense.modelo.torreta;

import org.towerdefense.modelo.Color;
import org.towerdefense.modelo.Posicion;

public final class TorretaPotente extends Torreta {

    public TorretaPotente(Posicion posicion) {
        super(posicion, 100, 1.0, 2, Color.ROJO, "potente", 100);
    }
}
