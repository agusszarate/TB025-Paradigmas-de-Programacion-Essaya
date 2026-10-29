package org.towerdefense.modelo.evento;

public interface Evento {
    void despachar(EventoListener listener);
}
