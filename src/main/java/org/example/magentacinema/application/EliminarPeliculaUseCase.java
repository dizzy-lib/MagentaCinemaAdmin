package org.example.magentacinema.application;

import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;

public class EliminarPeliculaUseCase {
    private final PeliculaService peliculaService;

    public EliminarPeliculaUseCase(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    public void ejecutar(Pelicula pelicula) {
        this.peliculaService.eliminarPelicula(pelicula.getId());
    }
}
