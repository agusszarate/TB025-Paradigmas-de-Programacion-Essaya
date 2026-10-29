package org.towerdefense.modelo.nivel;

import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.enemigo.Enemigo;
import org.towerdefense.modelo.enemigo.EnemigoDebil;
import org.towerdefense.modelo.enemigo.EnemigoFuerte;
import org.towerdefense.modelo.enemigo.EnemigoMedio;

public final class FabricaEnemigos {
    public Enemigo crear(String tipo, Posicion posicion) {
        return switch (tipo) {
            case "debil" -> new EnemigoDebil(posicion);
            case "medio" -> new EnemigoMedio(posicion);
            case "fuerte" -> new EnemigoFuerte(posicion);
            default -> throw new NivelInvalidoException("Tipo de enemigo desconocido: " + tipo);
        };
    }
}
