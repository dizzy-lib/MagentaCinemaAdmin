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
import org.example.magentacinema.application.ObtenerTodasLasPeliculasUseCase;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;
import org.example.magentacinema.presentation.view.MagentaApplication;
import org.example.magentacinema.shared.utils.DependecyFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModificarPeliculaController implements Initializable {
    private ObtenerTodasLasPeliculasUseCase obtenerTodasLasPeliculasUseCase;

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

    // Lista simulada de películas (en una app real vendría de base de datos)
    private ObservableList<Pelicula> todasLasPeliculas;
    private ObservableList<Pelicula> peliculasFiltradas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PeliculaService peliculaService = DependecyFactory.createPeliculaService();
        this.obtenerTodasLasPeliculasUseCase = new ObtenerTodasLasPeliculasUseCase(peliculaService);

        configurarTabla();
        cargarDatos();

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

    private void limpiarCampos() {
        this.titulo.clear();

        // Enfocar el primer campo
        this.titulo.requestFocus();
    }

    @FXML
    protected void onLimpiarCampos() {
        this.limpiarCampos();
    }

    private void configurarTabla() {
        // Configurar las columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("anno"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        // Configurar columna de acciones con botón editar
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Modificar");

            {
                btnEditar.setOnAction(event -> {
                    Pelicula pelicula = getTableView().getItems().get(getIndex());
                    editarPelicula(pelicula);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(btnEditar);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });

        // Inicializar listas
        peliculasFiltradas = FXCollections.observableArrayList();
        tablaResultados.setItems(peliculasFiltradas);
    }

    private void cargarDatos() {
        ObservableList<Pelicula> tes  = this.obtenerTodasLasPeliculasUseCase.ejecutar();

        // Datos de ejemplo (en una app real vendrían de base de datos)
        todasLasPeliculas = FXCollections.observableArrayList(tes);

        peliculasFiltradas.addAll(todasLasPeliculas);
    }

    @FXML
    protected void onBuscarClick() {
        filtrarPeliculas();
    }

    private void filtrarPeliculas() {
        String titulo = this.titulo.getText().toLowerCase().trim();

        List<Pelicula> resultados = todasLasPeliculas.stream()
                .filter(pelicula ->
                        (titulo.isEmpty() || pelicula.getTitulo().toLowerCase().contains(titulo))
                )
                .toList();

        peliculasFiltradas.clear();
        peliculasFiltradas.addAll(resultados);

        // Mostrar mensaje si no hay resultados
        if (resultados.isEmpty() && (!titulo.isEmpty())) {
            mostrarAlerta("Búsqueda", "No se encontraron películas que coincidan con los criterios de búsqueda");
        }
    }

    private void editarPelicula(Pelicula pelicula) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("editar-pelicula.fxml"));

            Parent root = fxmlLoader.load();

            System.out.println(pelicula);

            EditarPeliculaController controller = fxmlLoader.getController();
            controller.setPelicula(pelicula);

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar - Pelicula: " +  pelicula.getTitulo() + " - " + pelicula.getDirector() + " - " + pelicula.getAnno() );
        } catch (IOException e) {
            System.out.println("Error al cargar vista para editar película");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
