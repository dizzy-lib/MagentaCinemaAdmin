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
import org.example.magentacinema.domain.entities.Genero;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;
import org.example.magentacinema.presentation.view.MagentaApplication;
import org.example.magentacinema.shared.utils.DependecyFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListarPeliculasController implements Initializable {
    private ObtenerTodasLasPeliculasUseCase obtenerTodasLasPeliculasUseCase;

    // Campos de búsqueda
    @FXML private TextField titulo;
    @FXML private TextField director;
    @FXML private TextField anno;
    @FXML private ComboBox<Genero> cmbGenero;


    @FXML private Button btnVolver;
    @FXML private Button bntBuscar;
    @FXML private Button btnLimpiarCampos;

    // Tabla de resultados
    @FXML private TableView<Pelicula> tablaResultados;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colAnno;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, Void> colAcciones;

    // Lista simulada de películas (en una app real vendría de base de datos)
    private ObservableList<Pelicula> todasLasPeliculas;
    private ObservableList<Pelicula> peliculasFiltradas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PeliculaService peliculaService = DependecyFactory.createPeliculaService();
        this.obtenerTodasLasPeliculasUseCase = new ObtenerTodasLasPeliculasUseCase(peliculaService);

        configurarGenero();
        configurarValidacionAnio();

        configurarTabla();
        cargarDatos();

    }

    private void configurarValidacionAnio() {
        // Validación en tiempo real para el año
        this.anno.textProperty().addListener((_, oldValue, newValue) -> {
            // Solo permitir números
            if (!newValue.matches("\\d*")) {
                this.anno.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            // Limitar a 4 dígitos máximo
            if (newValue.length() > 4) {
                this.anno.setText(oldValue);
                return;
            }

            // Validar rango cuando tiene 4 dígitos
            if (newValue.length() == 4) {
                try {
                    int anno = Integer.parseInt(newValue);
                    if (anno < 1 || anno > 9999) {
                        this.anno.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    this.anno.setText(oldValue);
                }
            }
        });

        // Validación cuando pierde el foco
        this.anno.focusedProperty().addListener((obs, _, isNowFocused) -> {
            if (!isNowFocused && !this.anno.getText().isEmpty()) {
                validarAnnoCompleto();
            }
        });
    }

    private void validarAnnoCompleto() {
        String texto = this.anno.getText();
        if (texto.length() < 4 && !texto.isEmpty()) {
            mostrarAlerta("Error de validación",
                    "El año debe tener exactamente 4 dígitos");
            this.anno.requestFocus();
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

    private void configurarTabla() {
        // Configurar las columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("anno"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

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
        // Obtener valores de búsqueda de forma segura
        String tituloBusqueda = (this.titulo != null && this.titulo.getText() != null)
                ? this.titulo.getText().toLowerCase().trim()
                : "";

        String directorBusqueda = (this.director != null && this.director.getText() != null)
                ? this.director.getText().toLowerCase().trim()
                : "";

        String annoBusqueda = (this.anno != null && this.anno.getText() != null)
                ? this.anno.getText().trim()
                : "";

        Genero generoBusqueda = (this.cmbGenero != null)
                ? this.cmbGenero.getValue()
                : null;

        List<Pelicula> resultados = todasLasPeliculas.stream()
                .filter(pelicula -> {
                    // Filtro por título (búsqueda parcial)
                    boolean cumpleTitulo = tituloBusqueda.isEmpty() ||
                            pelicula.getTitulo().toLowerCase().contains(tituloBusqueda);

                    // Filtro por director (búsqueda parcial)
                    boolean cumpleDirector = directorBusqueda.isEmpty() ||
                            pelicula.getDirector().toLowerCase().contains(directorBusqueda);

                    // Filtro por año (coincidencia exacta)
                    boolean cumpleAnno = true;
                    if (!annoBusqueda.isEmpty()) {
                        try {
                            int annoInt = Integer.parseInt(annoBusqueda);
                            cumpleAnno = pelicula.getAnno() == annoInt;
                        } catch (NumberFormatException e) {
                            cumpleAnno = false;
                        }
                    }

                    // Filtro por género (coincidencia exacta)
                    boolean cumpleGenero = generoBusqueda == null ||
                            pelicula.getGenero().equals(generoBusqueda);

                    // La película debe cumplir TODOS los criterios especificados
                    return cumpleTitulo && cumpleDirector && cumpleAnno && cumpleGenero;
                })
                .toList();

        peliculasFiltradas.clear();
        peliculasFiltradas.addAll(resultados);

        // Mostrar mensaje si no hay resultados y al menos un criterio fue especificado
        boolean algunCriterioEspecificado = !tituloBusqueda.isEmpty() ||
                !directorBusqueda.isEmpty() ||
                !annoBusqueda.isEmpty() ||
                generoBusqueda != null;

        if (resultados.isEmpty() && algunCriterioEspecificado) {
            mostrarAlerta("Búsqueda", "No se encontraron películas que coincidan con los criterios de búsqueda");
        }
    }

    private void configurarGenero() {
        // Crear lista con null al inicio para representar "Todas"
        ObservableList<Genero> options = FXCollections.observableArrayList();
        options.add(null); // Opción "Todas"
        options.addAll(Genero.values());

        cmbGenero.setItems(options);
        cmbGenero.setEditable(false);

        // Configurar cómo se muestra cada elemento
        cmbGenero.setButtonCell(new ListCell<Genero>() {
            @Override
            protected void updateItem(Genero item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas");
                } else {
                    setText(item.toString());
                }
            }
        });

        cmbGenero.setCellFactory(param -> new ListCell<Genero>() {
            @Override
            protected void updateItem(Genero item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas");
                } else {
                    setText(item.toString());
                }
            }
        });

        // Seleccionar "Todas" por defecto
        cmbGenero.setValue(null);
    }

    private void limpiarCampos() {
        // Limpiar cada campo verificando que no sea null
        if (this.titulo != null) {
            this.titulo.clear();
        }

        if (this.director != null) {
            this.director.clear();
        }

        if (this.anno != null) {
            this.anno.clear();
        }

        if (this.cmbGenero != null) {
            this.cmbGenero.setValue(null); // Vuelve a "Todas"
        }

        // Recargar todas las películas en la tabla
        peliculasFiltradas.clear();
        peliculasFiltradas.addAll(todasLasPeliculas);

        // Enfocar el primer campo disponible
        if (this.titulo != null) {
            this.titulo.requestFocus();
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
