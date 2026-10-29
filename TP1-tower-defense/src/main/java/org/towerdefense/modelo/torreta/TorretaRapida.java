package org.towerdefense.modelo.torreta;

import org.towerdefense.modelo.Color;
import org.towerdefense.modelo.Posicion;

public final class TorretaRapida extends Torreta {

    public TorretaRapida(Posicion posicion) {
        super(posicion, 100, 0.5, 1, Color.AMARILLO, "rapida", 75);
    }
}
