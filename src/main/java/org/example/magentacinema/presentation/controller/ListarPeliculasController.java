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
    @FXML private TextField annoDesde;
    @FXML private TextField annoHasta;
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
        configurarValidacionAnios();

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

        String annoDesdeText = (this.annoDesde != null && this.annoDesde.getText() != null)
                ? this.annoDesde.getText().trim()
                : "";

        String annoHastaText = (this.annoHasta != null && this.annoHasta.getText() != null)
                ? this.annoHasta.getText().trim()
                : "";

        Genero generoBusqueda = (this.cmbGenero != null)
                ? this.cmbGenero.getValue()
                : null;

        // Parsear años del rango
        Integer annoDesde = null;
        Integer annoHasta = null;

        if (!annoDesdeText.isEmpty()) {
            try {
                annoDesde = Integer.parseInt(annoDesdeText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error de validación", "El año desde debe ser un número válido");
                return;
            }
        }

        if (!annoHastaText.isEmpty()) {
            try {
                annoHasta = Integer.parseInt(annoHastaText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error de validación", "El año hasta debe ser un número válido");
                return;
            }
        }

        // Validar que el rango sea coherente
        if (annoDesde != null && annoHasta != null && annoDesde > annoHasta) {
            mostrarAlerta("Error de validación", "El año desde no puede ser mayor que el año hasta");
            return;
        }

        final Integer annoDesdeF = annoDesde;
        final Integer annoHastaF = annoHasta;

        List<Pelicula> resultados = todasLasPeliculas.stream()
                .filter(pelicula -> {
                    // Filtro por título (búsqueda parcial)
                    boolean cumpleTitulo = tituloBusqueda.isEmpty() ||
                            pelicula.getTitulo().toLowerCase().contains(tituloBusqueda);

                    // Filtro por director (búsqueda parcial)
                    boolean cumpleDirector = directorBusqueda.isEmpty() ||
                            pelicula.getDirector().toLowerCase().contains(directorBusqueda);

                    // Filtro por rango de años
                    boolean cumpleAnno = true;
                    if (annoDesdeF != null) {
                        cumpleAnno = pelicula.getAnno() >= annoDesdeF;
                    }
                    if (annoHastaF != null) {
                        cumpleAnno = cumpleAnno && pelicula.getAnno() <= annoHastaF;
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
                !annoDesdeText.isEmpty() ||
                !annoHastaText.isEmpty() ||
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

    private void configurarValidacionAnios() {
        // Validación para año desde
        if (this.annoDesde != null) {
            configurarValidacionCampoAnio(this.annoDesde);
        }

        // Validación para año hasta
        if (this.annoHasta != null) {
            configurarValidacionCampoAnio(this.annoHasta);
        }
    }

    private void configurarValidacionCampoAnio(TextField campoAnio) {
        // Validación en tiempo real
        campoAnio.textProperty().addListener((_, oldValue, newValue) -> {
            // Solo permitir números
            if (!newValue.matches("\\d*")) {
                campoAnio.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            // Limitar a 4 dígitos máximo
            if (newValue.length() > 4) {
                campoAnio.setText(oldValue);
                return;
            }

            // Validar rango cuando tiene 4 dígitos
            if (newValue.length() == 4) {
                try {
                    int anno = Integer.parseInt(newValue);
                    if (anno < 1 || anno > 9999) {
                        campoAnio.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    campoAnio.setText(oldValue);
                }
            }
        });

        // Validación cuando pierde el foco
        campoAnio.focusedProperty().addListener((obs, _, isNowFocused) -> {
            if (!isNowFocused && !campoAnio.getText().isEmpty()) {
                String texto = campoAnio.getText();
                if (texto.length() < 4) {
                    mostrarAlerta("Error de validación",
                            "El año debe tener exactamente 4 dígitos");
                    campoAnio.requestFocus();
                }
            }
        });
    }



    private void limpiarCampos() {
        // Limpiar cada campo verificando que no sea null
        if (this.titulo != null) {
            this.titulo.clear();
        }

        if (this.director != null) {
            this.director.clear();
        }

        if (this.annoDesde != null) {
            this.annoDesde.clear();
        }

        if (this.annoHasta != null) {
            this.annoHasta.clear();
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
