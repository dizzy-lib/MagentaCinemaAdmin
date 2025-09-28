package org.example.magentacinema.infraestructure.interfaces;

import javafx.collections.ObservableList;
import org.example.magentacinema.domain.entities.Pelicula;

/**
 * Interfaz que maneja las operaciones que se pueden
 * realizar en el repositorio de cartelera
 */
public interface CarteleraRepository {
    /**
     * Agrega una película dentro de la cartelera
     * @param pelicula pelicula a agregar
     */
    void agregarPelicula(Pelicula pelicula);

    /**
     * Obtiene todas las películas dentro de la base de datos
     * @return todas las peliculas almacenadas
     */
    ObservableList<Pelicula> obtenerTodasLasPeliculas();

    /**
     * Actualiza una película (por su id)
     * @param id id de la película a actualizar
     * @param nuevaPelicula película con los datos actualizados
     */
    void actualizarPelicula(int id, Pelicula nuevaPelicula);

    /**
     * Elimina una película por su id
     * @param id id de la pelicula
     */
    void eliminarPelicula(int id);
}

