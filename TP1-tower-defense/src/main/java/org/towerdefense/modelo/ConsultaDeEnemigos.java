package org.towerdefense.modelo;

import org.towerdefense.modelo.enemigo.Enemigo;

public interface ConsultaDeEnemigos {
    Enemigo primeroEnRuta(Posicion desde, int rango);
}
