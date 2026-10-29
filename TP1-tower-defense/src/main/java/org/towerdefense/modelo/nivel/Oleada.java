package org.towerdefense.modelo.nivel;

import java.util.List;

public class Oleada {
    private final List<SpawnProgramado> spawns;

    public Oleada(List<SpawnProgramado> spawns) {
        this.spawns = List.copyOf(spawns);
    }

    public List<SpawnProgramado> spawns() {
        return spawns;
    }

    public int cantidad() {
        return spawns.size();
    }

}
