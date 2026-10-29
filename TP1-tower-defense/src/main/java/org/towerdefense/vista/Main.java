package org.towerdefense.vista;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.towerdefense.modelo.Partida;
import org.towerdefense.modelo.evento.EventoListener;
import org.towerdefense.modelo.evento.VictoriaDefinitiva;
import org.towerdefense.modelo.nivel.CargadorNivelXML;
import org.towerdefense.modelo.nivel.Nivel;
import org.towerdefense.modelo.nivel.NivelInvalidoException;
import org.towerdefense.vista.pantalla.PantallaCartel;
import org.towerdefense.vista.pantalla.PantallaJuego;
import org.towerdefense.vista.pantalla.PantallaMenu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main extends Application {

    private static final int TOTAL_NIVELES = 3;

    private Stage stage;
    private GameLoop gameLoopActivo;
    private ReproductorSonido reproductorSonido;
    private OrquestadorNiveles orquestador;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.reproductorSonido = new ReproductorSonido();
        this.orquestador = new OrquestadorNiveles(this, TOTAL_NIVELES);
        stage.setTitle("TP1 Tower Defense");
        stage.setResizable(false);
        mostrarMenu();
        reproductorSonido.iniciarMusica();
        stage.show();
    }

    @Override
    public void stop() {
        if (reproductorSonido != null) {
            reproductorSonido.liberar();
        }
    }

    public void mostrarMenu() {
        detenerGameLoop();
        stage.setScene(new PantallaMenu(this).scene());
    }

    public void iniciarNivel(int numero) {
        try {
            detenerGameLoop();
            Nivel nivel = cargarNivel(numero);

            EventoListener listener = new ListenerCompuesto(List.of(reproductorSonido, orquestador));
            Partida partida = new Partida(nivel, listener);
            PantallaJuego pantalla = new PantallaJuego(partida);
            Renderizador renderizador = new Renderizador(pantalla.canvas());
            stage.setScene(pantalla.scene());

            gameLoopActivo = new GameLoop(partida, () -> {
                renderizador.dibujar(partida);
                pantalla.actualizarHud();
            });
            gameLoopActivo.start();
        } catch (NivelInvalidoException e) {
            String xmlPath = "/niveles/nivel" + numero + ".xml";
            String mensajeError = String.format(
                    "Error al cargar el Nivel %d\nArchivo: %s\n\nDetalle: %s",
                    numero, xmlPath, e.getMessage()
            );
            mostrarError(mensajeError);
        }
    }

    public void mostrarVictoriaNivel(int nivel) {
        detenerGameLoop();
        PantallaCartel cartel = new PantallaCartel(
                "¡Nivel " + nivel + " completado!",
                "Preparate para el siguiente nivel.",
                "Continuar",
                () -> iniciarNivel(nivel + 1));
        stage.setScene(cartel.scene());
    }

    public void mostrarVictoriaDefinitiva() {
        detenerGameLoop();
        reproductorSonido.notificar(new VictoriaDefinitiva());
        PantallaCartel cartel = new PantallaCartel(
                "¡Victoria Definitiva!",
                "Completaste los 3 niveles. ¡Bien jugado!",
                "Volver al menú",
                this::mostrarMenu);
        stage.setScene(cartel.scene());
    }

    public void mostrarDerrota(int nivel) {
        detenerGameLoop();
        PantallaCartel cartel = new PantallaCartel(
                "Perdiste",
                "El nivel " + nivel + " fue tomado por los enemigos.",
                "Volver al menú",
                this::mostrarMenu);
        stage.setScene(cartel.scene());
    }

    public void mostrarError(String mensaje) {
        detenerGameLoop();
        PantallaCartel cartel = new PantallaCartel(
                "Error",
                mensaje,
                "Volver al menú",
                this::mostrarMenu);
        stage.setScene(cartel.scene());
    }

    public void salir() {
        Platform.exit();
    }

    private Nivel cargarNivel(int numero) {
        String xmlPath = "/niveles/nivel" + numero + ".xml";
        String xsdPath = "/niveles/nivel.xsd";
        try (InputStream xml = getClass().getResourceAsStream(xmlPath);
             InputStream xsd = getClass().getResourceAsStream(xsdPath)) {
            if (xml == null) {
                throw new NivelInvalidoException("No se pudo encontrar el archivo del nivel.");
            }
            if (xsd == null) {
                throw new NivelInvalidoException("No se pudo encontrar el esquema de validación (XSD).");
            }
            return new CargadorNivelXML().cargar(numero, xml, xsd);
        } catch (IOException e) {
            throw new NivelInvalidoException("Error de E/S al acceder a los archivos del nivel.", e);
        }
    }

    private void detenerGameLoop() {
        if (gameLoopActivo != null) {
            gameLoopActivo.stop();
            gameLoopActivo = null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
