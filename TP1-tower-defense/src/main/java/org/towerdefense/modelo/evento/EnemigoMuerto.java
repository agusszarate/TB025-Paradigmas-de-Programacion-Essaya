package org.towerdefense.modelo.evento;

import org.towerdefense.modelo.Posicion;

public record EnemigoMuerto(Posicion posicion) implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoEnemigoMuere(this);
    }
}
