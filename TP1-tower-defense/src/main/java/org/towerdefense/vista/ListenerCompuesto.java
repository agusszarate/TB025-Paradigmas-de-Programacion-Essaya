package org.towerdefense.vista;

import org.towerdefense.modelo.evento.Evento;
import org.towerdefense.modelo.evento.EventoListener;

import java.util.List;

public final class ListenerCompuesto implements EventoListener {

    private final List<EventoListener> listeners;

    public ListenerCompuesto(List<EventoListener> listeners) {
        this.listeners = List.copyOf(listeners);
    }

    @Override
    public void notificar(Evento evento) {
        for (EventoListener l : listeners) {
            l.notificar(evento);
        }
    }
}
