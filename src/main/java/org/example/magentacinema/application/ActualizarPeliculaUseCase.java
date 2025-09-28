package org.example.magentacinema.application;

import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;

public class ActualizarPeliculaUseCase {
    private final PeliculaService peliculaService;

    public ActualizarPeliculaUseCase(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    public void ejecutar(int id, Pelicula nuevaPelicula) {
        this.peliculaService.actualizarPelicula(id, nuevaPelicula);
    }
}
