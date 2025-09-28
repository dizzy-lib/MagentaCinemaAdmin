package org.example.magentacinema.domain.entities;

import org.example.magentacinema.shared.utils.Validaciones;

public class PeliculaBase implements Pelicula {
    private final int id;
    private String titulo;
    private String director;
    private int anno;
    private int duracion;
    private Genero genero;

    public PeliculaBase(
            int id,
            String titulo,
            String director,
            int anno,
            int duracion,
            Genero genero
    ) throws IllegalArgumentException {
        // Realiza validaciones de dominio
        Validaciones.validarPositivo(id);
        Validaciones.validarTexto(titulo);
        Validaciones.validarTexto(director);
        Validaciones.validarNumero(anno, 1000, 9999);
        Validaciones.validarPositivo(duracion);

        this.id = id;

        // Hace trim para que borre espacios al inicio
        // y fin de los datos cargados
        this.titulo = titulo.trim();
        this.director = director.trim();

        this.anno = anno;
        this.duracion = duracion;
        this.genero = genero;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitulo() {
        return this.titulo;
    }

    @Override
    public String getDirector() {
        return this.director;
    }

    @Override
    public int getAnno() {
        return this.anno;
    }

    @Override
    public int getDuracion() {
        return this.duracion;
    }

    @Override
    public Genero getGenero() {
        return this.genero;
    }

    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public void setAnno(int anno) {
        this.anno = anno;
    }

    @Override
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return String.format(
                "PeliculaBase{id=%d, titulo='%s', director='%s', anno=%d, duracion=%d min, genero=%s}",
                id,
                titulo,
                director,
                anno,
                duracion,
                genero != null ? genero : "No especificado"
        );
    }
}
