package org.towerdefense.modelo.nivel;

import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.torreta.Torreta;
import org.towerdefense.modelo.torreta.TorretaPotente;
import org.towerdefense.modelo.torreta.TorretaRapida;
import org.towerdefense.modelo.torreta.TorretaSimple;

public class FabricaTorretas {
    public Torreta crear(String tipo, Posicion posicion) {
        return switch (tipo) {
            case "simple" -> new TorretaSimple(posicion);
            case "rapida" -> new TorretaRapida(posicion);
            case "potente" -> new TorretaPotente(posicion);
            default -> throw new NivelInvalidoException("Tipo de torreta desconocido: " + tipo);
        };
    }
}
