package org.towerdefense.modelo;

import org.towerdefense.modelo.enemigo.Enemigo;
import org.towerdefense.modelo.evento.*;
import org.towerdefense.modelo.nivel.FabricaEnemigos;
import org.towerdefense.modelo.nivel.FabricaTorretas;
import org.towerdefense.modelo.nivel.Nivel;
import org.towerdefense.modelo.nivel.NivelInvalidoException;
import org.towerdefense.modelo.nivel.SpawnProgramado;
import org.towerdefense.modelo.nivel.TorretaInicial;
import org.towerdefense.modelo.proyectil.Proyectil;
import org.towerdefense.modelo.torreta.Torreta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Partida implements ConsultaDeEnemigos {
    private static final double DT_MAX = 0.1;

    private final int numero;
    private final Ruta ruta;
    private final List<Slot> slots;
    private final List<SpawnProgramado> spawns;
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Torreta> torretas = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final FabricaEnemigos fabricaEnemigos = new FabricaEnemigos();
    private final FabricaTorretas fabricaTorretas = new FabricaTorretas();
    private final EventoListener listener;

    private int dinero;
    private int puntaje;
    private int vidaBase;
    private final int vidaBaseMaxima;
    private double tiempoTranscurrido;
    private int proximoSpawnIndice;
    private boolean terminada;
    private boolean ganada;

    public Partida(Nivel nivel, EventoListener listener) {
        this.ruta = nivel.ruta();
        this.slots = nivel.slots();
        this.spawns = nivel.oleada().spawns();
        this.dinero = nivel.dineroInicial();
        this.vidaBase = nivel.vidaBaseInicial();
        this.vidaBaseMaxima = nivel.vidaBaseInicial();
        this.puntaje = 0;
        this.numero = nivel.numero();
        this.tiempoTranscurrido = 0.0;
        this.proximoSpawnIndice = 0;
        this.terminada = false;
        this.ganada = false;
        this.listener = listener;
        instalarTorretasIniciales(nivel.torretasIniciales());
    }

    private void instalarTorretasIniciales(List<TorretaInicial> iniciales) {
        for (TorretaInicial inicial : iniciales) {
            Slot slot = buscarSlotEn(inicial.posicion());
            Torreta torreta = fabricaTorretas.crear(inicial.tipo(), slot.posicion());
            slot.colocar(torreta);
            this.torretas.add(torreta);
        }
    }

    private Slot buscarSlotEn(Posicion posicion) {
        for (Slot s : slots) {
            if (s.posicion().equals(posicion)) {
                return s;
            }
        }
        throw new NivelInvalidoException("Torreta inicial sin slot en posición: " + posicion);
    }

    public void tick(double dt) {
        if (this.terminada) {
            return;
        }

        double delta = Math.min(dt, DT_MAX);
        tiempoTranscurrido += delta;

        spawnEnemigosPendientes();
        avanzarEnemigos(delta);
        if (this.terminada) {
            return;
        }
        disparoDeTorretas(delta);
        avanzarProyectiles(delta);
        evaluarFinDeNivel();
    }

    private  void spawnEnemigosPendientes() {
        long tiempoMS = (long) (tiempoTranscurrido * 1000);
        while (proximoSpawnIndice < spawns.size() && spawns.get(proximoSpawnIndice).delayMs() <= tiempoMS) {
            SpawnProgramado spawn = spawns.get(proximoSpawnIndice);
            Enemigo nuevo = fabricaEnemigos.crear(spawn.tipo(), ruta.puntoEn(0));
            enemigos.add(nuevo);
            proximoSpawnIndice++;
            listener.notificar(new EnemigoSpawn(nuevo.posicion(), nuevo.tipo()));
        }
    };

    private void avanzarEnemigos(double dt) {
        Iterator<Enemigo> it = this.enemigos.iterator();
        while (it.hasNext()) {
            Enemigo e = it.next();
            e.avanzar(dt, ruta);

            if (!e.estaVivo()) {
                this.dinero += e.recompensa();
                this.puntaje += 10;
                listener.notificar(new EnemigoMuerto(e.posicion()));
                it.remove();
                continue;
            }

            if (e.progreso() >= ruta.longitudTotal()) {
                this.vidaBase -= e.danioABase();
                listener.notificar(new EnemigoLlegoABase());
                it.remove();

                if (this.vidaBase <= 0) {
                    this.vidaBase = 0;
                    this.terminada = true;
                    listener.notificar(new NivelPerdido(this.numero));
                }
            }
        }
    }

    private void disparoDeTorretas(double dt) {
        for (Torreta t:  this.torretas) {
            Proyectil disparo = t.actualizar(dt, this);
            if (disparo != null) {
                proyectiles.add(disparo);
                listener.notificar(new ProyectilDisparado());
            }
        }
    }

    private void avanzarProyectiles(double dt) {
        Iterator<Proyectil> it = this.proyectiles.iterator();
        while (it.hasNext()) {
            Proyectil p = it.next();
            p.avanzar(dt);

            if (!p.estaActivo()) {
                it.remove();
            }
        }
    }

    private  void evaluarFinDeNivel() {
        if (this.terminada) {
            return;
        }

        if (proximoSpawnIndice >= spawns.size() && enemigos.isEmpty()) {
            this.terminada = true;
            this.ganada = true;
            listener.notificar(new NivelGanado(this.numero));
        }
    };

    @Override
    public Enemigo primeroEnRuta(Posicion desde, int rango) {
        Enemigo mejorBlanco = null;
        double maximoProgreso = -1.0;

        for (Enemigo e: enemigos) {
            double distancia = desde.distanciaA(e.posicion());

            if (e.estaVivo() && distancia <= rango) {
                if (e.progreso() > maximoProgreso) {
                    maximoProgreso = e.progreso();
                    mejorBlanco = e;
                }
            }
        }
        return mejorBlanco;
    }

    public void instalarTorreta(Slot slot, String tipo) {
        Torreta nueva = fabricaTorretas.crear(tipo, slot.posicion());

        if (this.dinero >= nueva.costo()) {
            this.dinero -= nueva.costo();
            if (!slot.estaVacio()) {
                this.torretas.remove(slot.torreta());
            }
            slot.colocar(nueva);
            this.torretas.add(nueva);
            listener.notificar(new TorretaInstalada(nueva.posicion(), nueva.tipo()));
        }
    }

    public List<Proyectil> proyectiles() {
        return this.proyectiles;
    }
    public List<Enemigo> enemigos() {
        return this.enemigos;
    }
    public List<Torreta> torretas() {
        return this.torretas;
    }
    public List<Slot> slots() {
        return slots;
    }
    public Ruta ruta() {
        return ruta;
    }
    public int dinero() {
        return dinero;
    }
    public int puntaje() {
        return puntaje;
    }
    public int vidaBase() {
        return vidaBase;
    }

    public int vidaBaseMaxima() {
        return vidaBaseMaxima;
    }
    public int numero() {
        return numero;
    }
    public boolean terminada() {
        return terminada;
    }
    public boolean ganada() {
        return ganada;
    }
}
