package org.example.magentacinema.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.magentacinema.presentation.view.MagentaApplication;

import java.io.IOException;

/**
 * Controlador del menú principal, aquí van las opciones principales que
 * se quieran controlar, en conjunto se debe crear el archivo fxml para
 * mostrar la vista de la escena
 */
public class MenuPrincipalController {
    @FXML private Button btnAgregarPelicula;
    @FXML private Button btnModificarPelicula;
    @FXML private Button btnEliminarPelicula;

    @FXML
    protected void onAgregarPelicula() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("agregar-pelicula.fxml"));

            Parent root = fxmlLoader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnAgregarPelicula.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Magenta Cinema - Agregar Pelicula");
        } catch (IOException e) {
            System.out.println("Error al cargar vista para agregar película");
        }
    }

    @FXML
    protected void onModificarPelicula() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("modificar-pelicula.fxml"));

            Parent root = fxmlLoader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnAgregarPelicula.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Magenta Cinema - Modificar Pelicula");
        } catch (IOException e) {
            System.out.println("Error al cargar vista para modificar película");
        }
    }

    @FXML
    protected void onEliminarPelicula() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MagentaApplication.class.getResource("eliminar-pelicula.fxml"));

            Parent root = fxmlLoader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnAgregarPelicula.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Magenta Cinema - Eliminar Película");
        } catch (IOException e) {
            System.out.println("Error al cargar vista para eliminar película");
        }
    }
}
