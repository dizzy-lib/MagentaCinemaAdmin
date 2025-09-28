package org.example.magentacinema.application;

import javafx.collections.ObservableList;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.services.PeliculaService;

public class ObtenerTodasLasPeliculasUseCase {
    private final PeliculaService peliculaService;

    public ObtenerTodasLasPeliculasUseCase(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    public ObservableList<Pelicula> ejecutar() {
        return this.peliculaService.obtenerTodasLasPeliculas();
    }
}
