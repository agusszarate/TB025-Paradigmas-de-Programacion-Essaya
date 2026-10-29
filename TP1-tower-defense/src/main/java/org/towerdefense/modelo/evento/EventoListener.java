package org.towerdefense.modelo.evento;

public interface EventoListener {
    default void notificar(Evento evento) {}

    default void cuandoEnemigoSpawnea(EnemigoSpawn evento) {}
    default void cuandoEnemigoMuere(EnemigoMuerto evento) {}
    default void cuandoEnemigoLlegaABase(EnemigoLlegoABase evento) {}
    default void cuandoTorretaInstalada(TorretaInstalada evento) {}
    default void cuandoSeDisparaProyectil(ProyectilDisparado evento) {}
    default void cuandoNivelSeGana(NivelGanado evento) {}
    default void cuandoNivelSePierde(NivelPerdido evento) {}
    default void cuandoSeGanaDefinitiva(VictoriaDefinitiva evento) {}
}
