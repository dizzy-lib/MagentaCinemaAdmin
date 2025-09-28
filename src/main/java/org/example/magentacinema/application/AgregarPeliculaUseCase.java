package org.example.magentacinema.application;

import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;

public class AgregarPeliculaUseCase {
    private final PeliculaService peliculaService;

    public AgregarPeliculaUseCase(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    public void ejecutar(Pelicula pelicula) {
        peliculaService.agregarPelicula(pelicula);
    }
}
