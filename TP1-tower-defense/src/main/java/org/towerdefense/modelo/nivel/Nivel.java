package org.towerdefense.modelo.nivel;

import org.towerdefense.modelo.Ruta;
import org.towerdefense.modelo.Slot;

import java.util.List;

public class Nivel {
    private final int numero;
    private final int dineroInicial;
    private final int vidaBaseInicial;
    private final Ruta ruta;
    private final List<Slot> slots;
    private final List<TorretaInicial> torretasIniciales;
    private final Oleada oleada;

    public Nivel(int numero, int dineroInicial, int vidaBaseInicial, Ruta ruta, List<Slot> slots,
                 List<TorretaInicial> torretasIniciales, Oleada oleada) {
        this.numero = numero;
        this.dineroInicial = dineroInicial;
        this.vidaBaseInicial = vidaBaseInicial;
        this.ruta = ruta;
        this.slots = List.copyOf(slots);
        this.torretasIniciales = List.copyOf(torretasIniciales);
        this.oleada = oleada;
    }

    public int numero() {
        return numero;
    }

    public int dineroInicial() {
        return dineroInicial;
    }

    public int vidaBaseInicial() {
        return vidaBaseInicial;
    }

    public Ruta ruta() {
        return ruta;
    }

    public List<Slot> slots() {
        return slots;
    }

    public List<TorretaInicial> torretasIniciales() {
        return torretasIniciales;
    }

    public Oleada oleada() {
        return oleada;
    }
}