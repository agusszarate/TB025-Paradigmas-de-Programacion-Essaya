package org.towerdefense.modelo.evento;

public record VictoriaDefinitiva() implements Evento {
    @Override
    public void despachar(EventoListener listener) {
        listener.cuandoSeGanaDefinitiva(this);
    }
}
