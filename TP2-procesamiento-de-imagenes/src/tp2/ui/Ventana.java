package tp2.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ventana extends Application {
    private List<String> nombreFiltros = new ArrayList<>();
    private Object imagenOriginal = null;
    private Object imagenProcesada = null;
    private Object pipelineActual = Logica.pipelineVacio();
    private File archivoActual = null;

    @Override
    public void start(Stage stage) {
        MenuBar barra = new MenuBar();
        Menu archivo = new Menu("Archivo");
        MenuItem menuAbrir = new MenuItem("Abrir…");
        MenuItem menuGuardar = new MenuItem("Guardar");
        MenuItem menuGuardarComo = new MenuItem("Guardar como…");
        MenuItem menuSalir = new MenuItem("Salir");

        archivo.getItems().addAll(menuAbrir, menuGuardar, menuGuardarComo, menuSalir);

        barra.getMenus().add(archivo);

        ImageView imagen = new ImageView();
        imagen.setPreserveRatio(true);
        ScrollPane centro = new ScrollPane(imagen);
        centro.setFitToWidth(true);
        centro.setFitToHeight(true);

        ComboBox<String> combo = new ComboBox<>(FXCollections.observableArrayList(Logica.filtros()));
        combo.setMaxWidth(Double.MAX_VALUE);

        VBox pipeline = new VBox(6);
        pipeline.setMinHeight(200);

        Button btnAgregar = new Button("Agregar al pipeline");
        Button btnAplicar = new Button("Aplicar pipeline");
        Button btnReset = new Button("Resetear pipeline");
        Label lblEstado = new Label("Listo");

        VBox panel = new VBox(10,
                new Label("Pipeline:"),
                pipeline,
                new Separator(),
                new Label("Filtro:"),
                combo,
                new Separator(),
                btnAgregar,
                btnAplicar,
                btnReset,
                new Separator(),
                lblEstado);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(240);



        menuAbrir.setOnAction(evento -> {
            FileChooser explorador = new FileChooser();
            explorador.setTitle("Seleccionar imagen");
            explorador.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png", "*jpg", "*.jpeg"));

            File archivoElegido = explorador.showOpenDialog(stage);

            if (archivoElegido != null) {
                archivoActual = archivoElegido;
                imagenOriginal = Logica.cargar(archivoElegido.getAbsolutePath());
                pipelineActual = Logica.pipelineVacio();
                nombreFiltros.clear();
                actualizarVistaPipeline(pipeline);
                Image imagenFX = (Image) Logica.aImagenFx(imagenOriginal);
                imagen.setImage(imagenFX);
            }
        });

        menuGuardarComo.setOnAction(evento -> {
            Object imagenParaGuardar = (imagenProcesada != null) ? imagenProcesada : imagenOriginal;
            if (imagenParaGuardar == null) {
                return;
            }

            FileChooser explorador = new FileChooser();
            explorador.setTitle("Guardar imagen como...");
            explorador.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png"));

            File archivoGuardado = explorador.showSaveDialog(stage);
            if (archivoGuardado != null) {
                Logica.guardar(imagenParaGuardar, archivoGuardado.getAbsolutePath(), "png");
            }

        });

        menuGuardar.setOnAction(evento -> {
            Object imagenParaGuardar = (imagenProcesada != null) ? imagenProcesada : imagenOriginal;
            if (imagenParaGuardar != null) {
                Logica.guardar(imagenParaGuardar, archivoActual.getAbsolutePath(), "png");
            }
        });

        menuSalir.setOnAction(evento -> {
            Platform.exit();
        });

        btnAgregar.setOnAction(evento -> {
            String filtroElegido = combo.getValue();

            if (filtroElegido != null) {
                pipelineActual = Logica.agregar(pipelineActual, filtroElegido);
                nombreFiltros.add(filtroElegido);
                actualizarVistaPipeline(pipeline);
            }
        });

        btnAplicar.setOnAction(evento -> {
            if (imagenOriginal == null) {
                return;
            }

            lblEstado.setText("Procesando...");
            stage.setTitle("TP2 — Procesador de imágenes - Procesando...");
            panel.setDisable(true);
            barra.setDisable(true);
            stage.getScene().setCursor(Cursor.WAIT);

            Logica.aplicarAsync(imagenOriginal, pipelineActual, resultado -> {
                imagenProcesada = resultado;
                Image imagenFX = (Image) Logica.aImagenFx(resultado);
                Platform.runLater(() -> {
                    imagen.setImage(imagenFX);
                    lblEstado.setText("Listo");
                    stage.setTitle("TP2 — Procesador de imágenes");
                    panel.setDisable(false);
                    barra.setDisable(false);
                    stage.getScene().setCursor(Cursor.DEFAULT);
                });
            });
        });

        btnReset.setOnAction(evento -> {
            pipelineActual = Logica.pipelineVacio();
            pipeline.getChildren().clear();
            nombreFiltros.clear();
            actualizarVistaPipeline(pipeline);
            if (imagenOriginal != null) {
                Image imagenReseteada = (Image) Logica.aImagenFx(imagenOriginal);
                imagen.setImage(imagenReseteada);
            }
        });

        BorderPane raiz = new BorderPane();
        raiz.setTop(barra);
        raiz.setCenter(centro);
        raiz.setRight(panel);

        stage.setTitle("TP2 — Procesador de imágenes");
        stage.setScene(new Scene(raiz, 900, 600));
        stage.show();
    }

    private void actualizarVistaPipeline(VBox pipeline) {
        pipeline.getChildren().clear();

        for (int i = 0; i < nombreFiltros.size(); i++) {
            int idx = i;
            String nombre = nombreFiltros.get(i);

            Label lblTexto = new Label((i + 1) + ". " + nombre);
            Button btnX = new Button("X");
            btnX.setStyle("-fx-font-size: 10px;");

            btnX.setOnAction(evento -> {
                nombreFiltros.remove(idx);
                pipelineActual = Logica.quitar(pipelineActual, idx);
                actualizarVistaPipeline(pipeline);
            });

            HBox filtro = new HBox(10, lblTexto, btnX);
            filtro.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            pipeline.getChildren().add(filtro);

        }
    }
}
