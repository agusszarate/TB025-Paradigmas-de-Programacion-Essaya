package org.towerdefense.modelo.evento;

import org.towerdefense.modelo.Posicion;

public record TorretaInstalada(Posicion posicion, String tipo) implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoTorretaInstalada(this);
    }
}
