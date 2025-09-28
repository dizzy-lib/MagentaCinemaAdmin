package org.example.magentacinema.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.magentacinema.application.EliminarPeliculaUseCase;
import org.example.magentacinema.application.ObtenerTodasLasPeliculasUseCase;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;
import org.example.magentacinema.presentation.view.MagentaApplication;
import org.example.magentacinema.shared.utils.DependecyFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EliminarPeliculaController implements Initializable {
    private ObtenerTodasLasPeliculasUseCase obtenerTodasLasPeliculasUseCase;
    private EliminarPeliculaUseCase eliminarPeliculaUseCase;

    @FXML private TextField titulo;
    @FXML private Button btnVolver;
    @FXML private Button bntBuscar;
    @FXML private TableView<Pelicula> tablaResultados;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colAnno;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, Void> colAcciones;
    @FXML private Button btnLimpiarCampos;


    // Listas para manejar las películas
    private ObservableList<Pelicula> todasLasPeliculas;
    private ObservableList<Pelicula> peliculasFiltradas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PeliculaService peliculaService = DependecyFactory.createPeliculaService();
        this.obtenerTodasLasPeliculasUseCase = new ObtenerTodasLasPeliculasUseCase(peliculaService);
        this.eliminarPeliculaUseCase = new EliminarPeliculaUseCase(peliculaService);

        // Inicializar las listas ANTES de configurar la tabla
        todasLasPeliculas = FXCollections.observableArrayList();
        peliculasFiltradas = FXCollections.observableArrayList();

        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        // Configurar las columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("anno"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        // Configurar columna de acciones con botón eliminar
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setOnAction(event -> {
                    Pelicula pelicula = getTableView().getItems().get(getIndex());
                    eliminarPelicula(pelicula);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(btnEliminar);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });

        // Vincular la lista filtrada a la tabla
        tablaResultados.setItems(peliculasFiltradas);
    }

    private void cargarDatos() {
        try {
            // Obtener películas desde el caso de uso
            ObservableList<Pelicula> peliculasObtenidas = this.obtenerTodasLasPeliculasUseCase.ejecutar();

            // Limpiar y actualizar la lista principal
            todasLasPeliculas.clear();
            todasLasPeliculas.addAll(peliculasObtenidas);

            // Limpiar y actualizar la lista filtrada
            peliculasFiltradas.clear();
            peliculasFiltradas.addAll(todasLasPeliculas);

            // Forzar actualización de la tabla
            tablaResultados.refresh();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar las películas: " + e.getMessage());
        }
    }

    private void filtrarPeliculas() {
        String tituloFiltro = this.titulo.getText().toLowerCase().trim();

        List<Pelicula> resultados = todasLasPeliculas.stream()
                .filter(pelicula ->
                        (tituloFiltro.isEmpty() || pelicula.getTitulo().toLowerCase().contains(tituloFiltro))
                )
                .toList();

        peliculasFiltradas.clear();
        peliculasFiltradas.addAll(resultados);

        // Mostrar mensaje si no hay resultados
        if (resultados.isEmpty() && (!tituloFiltro.isEmpty())) {
            mostrarAlerta("Búsqueda", "No se encontraron películas que coincidan con los criterios de búsqueda");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void eliminarPelicula(Pelicula pelicula) {
        // Confirmar eliminación antes de proceder
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar esta película?");
        confirmacion.setContentText("Película: " + pelicula.getTitulo());

        // Mostrar diálogo de confirmación y esperar respuesta
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                // Ejecutar caso de uso para eliminar
                this.eliminarPeliculaUseCase.ejecutar(pelicula);

                // Mostrar mensaje de éxito
                mostrarAlerta("Éxito", "Película '" + pelicula.getTitulo() + "' eliminada con éxito");

                // Actualizar la tabla preservando filtros si los hay
                actualizarTabla();

            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar la película: " + e.getMessage());
            }
        }
    }

    private void actualizarTabla() {
        // Guardar el filtro actual
        String filtroActual = titulo.getText().trim();

        // Recargar todos los datos
        cargarDatos();

        // Si había un filtro aplicado, volver a aplicarlo
        if (!filtroActual.isEmpty()) {
            filtrarPeliculas();
        }
    }

    @FXML
    protected void onVolver() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("menu-principal.fxml"));

            Parent root = fxmlLoader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Magenta Cinema");
        } catch (IOException e) {
            System.out.println("Error al cargar vista para agregar película");
        }
    }

    @FXML
    protected void onLimpiarCampos() {
        this.limpiarCampos();
    }

    private void limpiarCampos() {
        this.titulo.clear();

        // Enfocar el primer campo
        this.titulo.requestFocus();
    }

    @FXML
    protected void onBuscarClick() {
        filtrarPeliculas();
    }
}