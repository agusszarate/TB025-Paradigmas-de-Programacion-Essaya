package org.towerdefense.modelo.evento;

public record ProyectilDisparado() implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoSeDisparaProyectil(this);
    }
}
