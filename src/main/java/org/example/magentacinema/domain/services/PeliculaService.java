package org.example.magentacinema.domain.services;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.infraestructure.interfaces.CarteleraRepository;

/**
 * Servicio de películas se utilizará siempre en casos de uso, por lo tanto, la mayoría de
 * métodos utilitarios para llevar a cabo los casos de uso tendrán que ser construídos aqui.
 *
 * En este clase se combina la capa de infraestructura con la de dominio para llevar a
 * cabo acciones precisas que posteriormente serán manejadas por controladores en la
 * capa de presentación utilizando el facade-adaptador de la capa de aplicación (casos de uso)
 */
public class PeliculaService {
    private final CarteleraRepository carteleraRepository;

    public PeliculaService(CarteleraRepository carteleraRepository) {
        this.carteleraRepository = carteleraRepository;
    }

    /**
     * Method que valida una película y la agrega dentro de la base de datos
     * mediante el repositorio de cartelera.
     * @param pelicula pelicula a agregar a la base de datos
     */
    public void agregarPelicula(Pelicula pelicula) {
        // Validaciones de negocio ANTES de persistir
        validarPelicula(pelicula);

        try {
            carteleraRepository.agregarPelicula(pelicula);
            System.out.println("[Éxito] Película agregada con éxito: " + pelicula.getTitulo());
        } catch (RuntimeException e) {
            System.err.println("[Error] No se pudo agregar la película: " + e.getMessage());
            throw new RuntimeException("Error en el proceso de agregar película", e);
        }
    }

    /**
     * Méthod para validar la lógica de negocio en cuanto a la utilización de transacciones
     * con películas
     * @param pelicula película a validar
     */
    private void validarPelicula(Pelicula pelicula) {
        // La película no puede ser Nullable
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }

        // El título de la pelicula tiene que estar presente y no puede ser string vacío
        if (pelicula.getTitulo() == null || pelicula.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la película es obligatorio");
        }

        // El director de la película tiene que estar presente y no puede estar vacío
        if (pelicula.getDirector() == null || pelicula.getDirector().trim().isEmpty()) {
            throw new IllegalArgumentException("El director de la película es obligatorio");
        }

        // Los años posibles de una película debe estar entre 1900 y 2030
        // TODO: agregar año actual como máximo
        if (pelicula.getAnno() < 1900 || pelicula.getAnno() > 2030) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2030");
        }

        // La duración debe ser mayor a 0 minutos
        if (pelicula.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
    }

    /**
     * Method que obtiene todas las películas de la base de datos
     * @return todas las películas de la base de datos
     */
    public ObservableList<Pelicula> obtenerTodasLasPeliculas() {
        return this.carteleraRepository.obtenerTodasLasPeliculas();
    }

    /**
     * Actualiza una película dado su id
     * @param id id de la pelicula a actualizar
     * @param nuevaPelicula nueva película con campos actualizados
     */
    public void actualizarPelicula(int id, Pelicula nuevaPelicula) {
        this.carteleraRepository.actualizarPelicula(id, nuevaPelicula);
    }

    /**
     * Method que elimina una película por su id
     * @param id id de película a eliminar
     */
    public void eliminarPelicula(int id) {
        this.carteleraRepository.eliminarPelicula(id);
    }
}