package org.towerdefense.vista;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.towerdefense.modelo.evento.EnemigoMuerto;
import org.towerdefense.modelo.evento.EnemigoSpawn;
import org.towerdefense.modelo.evento.Evento;
import org.towerdefense.modelo.evento.EventoListener;
import org.towerdefense.modelo.evento.NivelGanado;
import org.towerdefense.modelo.evento.NivelPerdido;
import org.towerdefense.modelo.evento.TorretaInstalada;
import org.towerdefense.modelo.evento.VictoriaDefinitiva;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class ReproductorSonido implements EventoListener {

    private static final String CLIP_SPAWN = "spawn";
    private static final String CLIP_MUERTE = "muerte_enemigo";
    private static final String CLIP_INSTALACION = "torreta_instalada";
    private static final String CLIP_VICTORIA_NIVEL = "victoria_nivel";
    private static final String CLIP_DERROTA_NIVEL = "derrota_nivel";
    private static final String CLIP_VICTORIA_DEFINITIVA = "victoria_definitiva";
    private static final String RUTA_MUSICA = "/audio/musica.mp3";
    private static final double VOLUMEN_MUSICA = 0.02;
    private static final double VOLUMEN_EFECTOS = 0.15;

    private final Map<String, AudioClip> clips = new HashMap<>();
    private MediaPlayer musica;

    public ReproductorSonido() {
        cargarMusica();
    }

    public void iniciarMusica() {
        if (musica != null) {
            musica.play();
        }
    }

    public void liberar() {
        if (musica != null) {
            musica.stop();
            musica.dispose();
            musica = null;
        }
    }

    @Override
    public void notificar(Evento evento) {
        evento.despachar(this);
    }

    @Override
    public void cuandoEnemigoSpawnea(EnemigoSpawn evento) {
        reproducir(CLIP_SPAWN);
    }

    @Override
    public void cuandoEnemigoMuere(EnemigoMuerto evento) {
        reproducir(CLIP_MUERTE);
    }

    @Override
    public void cuandoTorretaInstalada(TorretaInstalada evento) {
        reproducir(CLIP_INSTALACION);
    }

    @Override
    public void cuandoNivelSeGana(NivelGanado evento) {
        reproducir(CLIP_VICTORIA_NIVEL);
    }

    @Override
    public void cuandoNivelSePierde(NivelPerdido evento) {
        reproducir(CLIP_DERROTA_NIVEL);
    }

    @Override
    public void cuandoSeGanaDefinitiva(VictoriaDefinitiva evento) {
        reproducir(CLIP_VICTORIA_DEFINITIVA);
    }

    private void reproducir(String nombre) {
        AudioClip clip = clips.computeIfAbsent(nombre, this::cargarClip);
        if (clip != null) {
            clip.play(VOLUMEN_EFECTOS);
        }
    }

    private AudioClip cargarClip(String nombre) {
        URL url = getClass().getResource("/audio/" + nombre + ".wav");
        if (url == null) {
            return null;
        }
        try {
            return new AudioClip(url.toExternalForm());
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private void cargarMusica() {
        URL url = getClass().getResource(RUTA_MUSICA);
        if (url == null) {
            return;
        }
        try {
            Media media = new Media(url.toExternalForm());
            musica = new MediaPlayer(media);
            musica.setCycleCount(MediaPlayer.INDEFINITE);
            musica.setVolume(VOLUMEN_MUSICA);
        } catch (RuntimeException ex) {
            musica = null;
        }
    }
}
