package org.example.magentacinema.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.magentacinema.application.ActualizarPeliculaUseCase;
import org.example.magentacinema.domain.entities.Genero;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.entities.PeliculaBase;
import org.example.magentacinema.domain.services.PeliculaService;
import org.example.magentacinema.presentation.view.MagentaApplication;
import org.example.magentacinema.shared.utils.DependecyFactory;

import java.io.IOException;

public class EditarPeliculaController {
    private ActualizarPeliculaUseCase actualizarPeliculaUseCase;
    private Pelicula pelicula;

    @FXML private Button btnVolver;
    @FXML private Button btnModificar;

    @FXML private TextField titulo;
    @FXML private TextField director;
    @FXML private TextField duracion;
    @FXML private TextField anno;
    @FXML private ComboBox<Genero> cmbGenero;

    @FXML
    public void initialize() {
        configurarGenero();
        configurarValidacionAnio();
        configurarValidacionDuracion();

        PeliculaService peliculaService = DependecyFactory.createPeliculaService();
        this.actualizarPeliculaUseCase = new ActualizarPeliculaUseCase(peliculaService);
    }

    private void configurarGenero() {
        ObservableList<Genero> options = FXCollections.observableArrayList(Genero.values());
        cmbGenero.setItems(options);
        cmbGenero.setEditable(false);
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        cargarDatosPelicula();
    }

    // Cargar los datos de la película en el formulario
    private void cargarDatosPelicula() {
        if (pelicula != null) {
            titulo.setText(pelicula.getTitulo());
            director.setText(pelicula.getDirector());
            anno.setText(String.valueOf(pelicula.getAnno()));
            duracion.setText(String.valueOf(pelicula.getDuracion()));
            cmbGenero.setValue(pelicula.getGenero());
        }
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
                    int año = Integer.parseInt(newValue);
                    if (año < 1 || año > 9999) {
                        this.anno.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    this.anno.setText(oldValue);
                }
            }
        });
    }

    private void configurarValidacionDuracion() {
        // Validación en tiempo real para la duración
        this.duracion.textProperty().addListener((observable, oldValue, newValue) -> {
            // Solo permitir números
            if (!newValue.matches("\\d*")) {
                this.duracion.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }

            // Limitar a 3 dígitos máximo (hasta 999 minutos)
            if (newValue.length() > 3) {
                this.duracion.setText(oldValue);
                return;
            }

            // Validar rango mientras escribe
            if (!newValue.isEmpty()) {
                try {
                    int duracion = Integer.parseInt(newValue);
                    if (duracion < 1 || duracion > 999) {
                        this.duracion.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    this.duracion.setText(oldValue);
                }
            }
        });
    }

    private Integer obtenerAnno() {
        try {
            String texto = this.anno.getText().trim();
            if (texto.length() != 4) {
                return null;
            }
            int anno = Integer.parseInt(texto);
            return (anno >= 1 && anno <= 9999) ? anno : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer obtenerDuracion() {
        try {
            String texto = this.duracion.getText().trim();
            if (texto.isEmpty()) {
                return null;
            }
            int duracion = Integer.parseInt(texto);
            return (duracion >= 1 && duracion <= 999) ? duracion : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean validarCamposObligatorios() {
        if (this.titulo.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El título es obligatorio");
            this.titulo.requestFocus();
            return false;
        }

        if (this.director.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El director es obligatorio");
            this.director.requestFocus();
            return false;
        }

        if (this.anno.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El año es obligatorio");
            this.anno.requestFocus();
            return false;
        }

        if (cmbGenero.getValue() == null) {
            mostrarAlerta("Error", "Debe seleccionar un género");
            cmbGenero.requestFocus();
            return false;
        }

        if (this.duracion.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "La duración es obligatoria");
            this.duracion.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert;
        if (titulo.equals("Éxito")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
        }

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void onModificar() {
        int idPelicula = this.pelicula.getId();

        // Validar campos obligatorios
        if (!validarCamposObligatorios()) {
            return;
        }

        // Crea la película
        String titulo = this.titulo.getText();
        String director = this.director.getText();
        Integer duracion = this.obtenerDuracion();
        Integer anno = this.obtenerAnno();
        Genero genero = this.cmbGenero.getValue();

        if (anno == null) {
            this.mostrarAlerta("Error", "Debe ingresar un año valido (4 dígitos, entre 1000-9999)");
            this.anno.requestFocus();
            return;
        }

        if (duracion == null) {
            this.mostrarAlerta("Error", "Debe ingresar una duración válida (1-999 minutos)");
            this.anno.requestFocus();
            return;
        }

        Pelicula nuevaPelicula = new PeliculaBase(idPelicula, titulo, director, anno, duracion, genero);
        actualizarPeliculaUseCase.ejecutar(idPelicula, nuevaPelicula );

        String mensaje = String.format(
                "Película actualizada exitosamente:\n\n" +
                        "Título: %s\n" +
                        "Director: %s\n" +
                        "Año: %d\n" +
                        "Género: %s\n" +
                        "Duración: %d minutos",
                titulo, director, anno, genero, duracion
        );

        this.mostrarAlerta("Éxito", mensaje);

        // volver
        onVolver();
    }

    @FXML protected void onVolver() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("modificar-pelicula.fxml"));

            Parent root = fxmlLoader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Magenta Cinema - Modificar Pelicula");
        } catch (IOException e) {
            System.out.println("Error al cargar vista para modificar película");
        }
    }
}
