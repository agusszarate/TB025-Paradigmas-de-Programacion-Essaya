package org.towerdefense.modelo.evento;

public record NivelGanado(int nivel) implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoNivelSeGana(this);
    }
}
