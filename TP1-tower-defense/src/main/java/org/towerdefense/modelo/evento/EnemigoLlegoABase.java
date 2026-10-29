package org.towerdefense.modelo.evento;

public record EnemigoLlegoABase() implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoEnemigoLlegaABase(this);
    }
}
