package org.towerdefense.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.towerdefense.modelo.Partida;
import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.Ruta;
import org.towerdefense.modelo.Slot;
import org.towerdefense.modelo.enemigo.Enemigo;
import org.towerdefense.modelo.proyectil.Proyectil;
import org.towerdefense.modelo.torreta.Torreta;

import java.util.List;
import java.util.Map;

public final class Renderizador {

    private static final double ANCHO = 800;
    private static final double ALTO = 600;
    private static final double GROSOR_RUTA = 30;
    private static final double RADIO_ENEMIGO = 12;
    private static final double RADIO_PROYECTIL = 5;
    private static final double LADO_TORRETA = 22;
    private static final double LADO_SLOT = 26;
    private static final double LADO_BASE = 40;
    private static final double LADO_SPRITE_TORRETA = 36;
    private static final double LADO_SPRITE_ENEMIGO = 32;

    private static final Color COLOR_FALLBACK = Color.GRAY;

    private static final Map<String, Color> COLORES_TORRETA = Map.of(
            "simple", Color.GOLD,
            "rapida", Color.LIGHTGREEN,
            "potente", Color.CRIMSON);

    private static final Map<String, Color> COLORES_ENEMIGO = Map.of(
            "debil", Color.LIGHTSKYBLUE,
            "medio", Color.ORANGE,
            "fuerte", Color.DARKMAGENTA);

    private static final Map<org.towerdefense.modelo.Color, Color> COLORES_PROYECTIL = Map.of(
            org.towerdefense.modelo.Color.AMARILLO, Color.YELLOW,
            org.towerdefense.modelo.Color.ROJO, Color.RED);

    private static final Map<String, Image> SPRITES_TORRETA = Map.of(
            "simple", cargar("Armas/mk1/mk1_1.png"),
            "rapida", cargar("Armas/mk2/mk2_1.png"),
            "potente", cargar("Armas/mk3/mk3_1.png"));

    private static final Map<String, Image> SPRITES_ENEMIGO = Map.of(
            "debil", cargar("Enemigos/avion.gif"),
            "medio", cargar("Enemigos/tanque.gif"),
            "fuerte", cargar("Enemigos/bunker.gif"));

    private static final Image SPRITE_FONDO = cargar("Terrenos/terreno.png");
    private static final Image SPRITE_BASE_PROPIA = cargar("Terrenos/base.png");
    private static final Image SPRITE_SLOT = cargar("Terrenos/torre.png");

    private static Image cargar(String nombre) {
        var url = Renderizador.class.getResource("/sprites/" + nombre);
        return url == null ? null : new Image(url.toExternalForm());
    }

    private final GraphicsContext g;

    public Renderizador(Canvas canvas) {
        this.g = canvas.getGraphicsContext2D();
    }

    public void dibujar(Partida partida) {
        fondo();
        dibujarRuta(partida.ruta());
        dibujarBases(partida.ruta());
        dibujarBarraBase(partida);
        dibujarSlots(partida.slots());
        dibujarTorretas(partida.torretas());
        dibujarEnemigos(partida.enemigos(), partida.ruta());
        dibujarProyectiles(partida.proyectiles());
    }

    private void fondo() {
        if (SPRITE_FONDO != null) {
            double w = SPRITE_FONDO.getWidth();
            double h = SPRITE_FONDO.getHeight();
            for (double y = 0; y < ALTO; y += h) {
                for (double x = 0; x < ANCHO; x += w) {
                    g.drawImage(SPRITE_FONDO, x, y);
                }
            }
            return;
        }
        g.setFill(Color.rgb(30, 50, 30));
        g.fillRect(0, 0, ANCHO, ALTO);
    }

    private void dibujarRuta(Ruta ruta) {
        List<Posicion> puntos = ruta.puntos();
        g.setStroke(Color.SADDLEBROWN);
        g.setLineWidth(GROSOR_RUTA);
        for (int i = 1; i < puntos.size(); i++) {
            Posicion a = puntos.get(i - 1);
            Posicion b = puntos.get(i);
            g.strokeLine(a.x(), a.y(), b.x(), b.y());
        }
    }

    private void dibujarBases(Ruta ruta) {
        Posicion inicio = ruta.puntoEn(0);
        Posicion fin = ruta.puntoEn(ruta.longitudTotal());
        dibujarBase(null, Color.DARKRED, inicio);
        dibujarBase(SPRITE_BASE_PROPIA, Color.LIMEGREEN, fin);
    }

    private void dibujarBase(Image sprite, Color fallback, Posicion p) {
        double x = p.x() - LADO_BASE / 2;
        double y = p.y() - LADO_BASE / 2;
        if (sprite != null) {
            g.drawImage(sprite, x, y, LADO_BASE, LADO_BASE);
            return;
        }
        g.setFill(fallback);
        g.fillRect(x, y, LADO_BASE, LADO_BASE);
    }

    private void dibujarSlots(List<Slot> slots) {
        for (Slot s : slots) {
            if (!s.estaVacio()) {
                continue;
            }
            double x = s.posicion().x() - LADO_SLOT / 2;
            double y = s.posicion().y() - LADO_SLOT / 2;
            if (SPRITE_SLOT != null) {
                g.drawImage(SPRITE_SLOT, x, y, LADO_SLOT, LADO_SLOT);
                continue;
            }
            g.setFill(Color.rgb(220, 220, 220, 0.35));
            g.fillRect(x, y, LADO_SLOT, LADO_SLOT);
            g.setStroke(Color.rgb(240, 240, 240, 0.8));
            g.setLineWidth(1.5);
            g.strokeRect(x, y, LADO_SLOT, LADO_SLOT);
        }
    }

    private void dibujarTorretas(List<Torreta> torretas) {
        for (Torreta t : torretas) {
            double cx = t.posicion().x();
            double cy = t.posicion().y();
            g.setStroke(Color.rgb(255, 255, 255, 0.18));
            g.setLineWidth(1);
            g.strokeOval(cx - t.rango(), cy - t.rango(), t.rango() * 2, t.rango() * 2);

            Image sprite = SPRITES_TORRETA.get(t.tipo());
            if (sprite != null) {
                double x = cx - LADO_SPRITE_TORRETA / 2;
                double y = cy - LADO_SPRITE_TORRETA / 2;
                g.drawImage(sprite, x, y, LADO_SPRITE_TORRETA, LADO_SPRITE_TORRETA);
                continue;
            }
            double x = cx - LADO_TORRETA / 2;
            double y = cy - LADO_TORRETA / 2;
            g.setFill(COLORES_TORRETA.getOrDefault(t.tipo(), COLOR_FALLBACK));
            g.fillRect(x, y, LADO_TORRETA, LADO_TORRETA);
            g.setStroke(Color.BLACK);
            g.setLineWidth(2);
            g.strokeRect(x, y, LADO_TORRETA, LADO_TORRETA);
        }
    }

    private void dibujarEnemigos(List<Enemigo> enemigos, org.towerdefense.modelo.Ruta ruta) {
        for (Enemigo e : enemigos) {
            double cx = e.posicion().x();
            double cy = e.posicion().y();
            Image sprite = SPRITES_ENEMIGO.get(e.tipo());
            if (sprite != null) {
                double anguloGrados = anguloDeMovimiento(ruta, e.progreso(), cx, cy);
                g.save();
                g.translate(cx, cy);
                g.rotate(anguloGrados);
                g.drawImage(sprite, -LADO_SPRITE_ENEMIGO / 2, -LADO_SPRITE_ENEMIGO / 2,
                        LADO_SPRITE_ENEMIGO, LADO_SPRITE_ENEMIGO);
                g.restore();
            } else {
                double x = cx - RADIO_ENEMIGO;
                double y = cy - RADIO_ENEMIGO;
                g.setFill(COLORES_ENEMIGO.getOrDefault(e.tipo(), COLOR_FALLBACK));
                g.fillOval(x, y, RADIO_ENEMIGO * 2, RADIO_ENEMIGO * 2);
                g.setStroke(Color.BLACK);
                g.setLineWidth(1);
                g.strokeOval(x, y, RADIO_ENEMIGO * 2, RADIO_ENEMIGO * 2);
            }
            dibujarBarraVidaEnemigo(e, cx, cy);
        }
    }

    private double anguloDeMovimiento(org.towerdefense.modelo.Ruta ruta, double progreso, double cx, double cy) {
        Posicion siguiente = ruta.puntoEn(progreso + 1.0);
        double dx = siguiente.x() - cx;
        double dy = siguiente.y() - cy;
        if (dx == 0 && dy == 0) return 0;
        return Math.toDegrees(Math.atan2(dy, dx)) + 90;
    }

    private void dibujarBarraVidaEnemigo(Enemigo e, double cx, double cy) {
        if (e.vidaMaxima() <= 0) return;
        double ancho = LADO_SPRITE_ENEMIGO;
        double alto = 4;
        double x = cx - ancho / 2;
        double y = cy - LADO_SPRITE_ENEMIGO / 2 - alto - 2;
        double porcentaje = Math.max(0, Math.min(1.0, (double) e.vida() / e.vidaMaxima()));
        g.setFill(Color.rgb(0, 0, 0, 0.6));
        g.fillRect(x, y, ancho, alto);
        g.setFill(porcentaje > 0.5 ? Color.LIMEGREEN : porcentaje > 0.25 ? Color.GOLD : Color.CRIMSON);
        g.fillRect(x, y, ancho * porcentaje, alto);
    }

    private void dibujarBarraBase(Partida partida) {
        int max = partida.vidaBaseMaxima();
        if (max <= 0) return;
        double porcentaje = Math.max(0, Math.min(1.0, (double) partida.vidaBase() / max));
        Posicion fin = partida.ruta().puntoEn(partida.ruta().longitudTotal());
        double ancho = 50;
        double alto = 6;
        double x = fin.x() - ancho / 2;
        double y = fin.y() - LADO_BASE / 2 - alto - 4;
        g.setFill(Color.rgb(0, 0, 0, 0.7));
        g.fillRect(x, y, ancho, alto);
        g.setFill(porcentaje > 0.5 ? Color.LIMEGREEN : porcentaje > 0.25 ? Color.GOLD : Color.CRIMSON);
        g.fillRect(x, y, ancho * porcentaje, alto);
        g.setStroke(Color.WHITE);
        g.setLineWidth(1);
        g.strokeRect(x, y, ancho, alto);
    }

    private void dibujarProyectiles(List<Proyectil> proyectiles) {
        for (Proyectil p : proyectiles) {
            double cx = p.posicion().x();
            double cy = p.posicion().y();
            double halo = RADIO_PROYECTIL + 2;
            g.setFill(Color.WHITE);
            g.fillOval(cx - halo, cy - halo, halo * 2, halo * 2);
            g.setFill(COLORES_PROYECTIL.getOrDefault(p.color(), COLOR_FALLBACK));
            g.fillOval(cx - RADIO_PROYECTIL, cy - RADIO_PROYECTIL, RADIO_PROYECTIL * 2, RADIO_PROYECTIL * 2);
        }
    }
}
