package org.towerdefense.modelo.evento;

public record NivelPerdido(int nivel) implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoNivelSePierde(this);
    }
}
