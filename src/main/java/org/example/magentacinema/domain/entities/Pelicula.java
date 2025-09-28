package org.example.magentacinema.domain.entities;

public interface Pelicula {
    int getId();
    String getTitulo();
    String getDirector();
    int getAnno();
    int getDuracion();
    Genero getGenero();
    void setTitulo(String titulo);
    void setDirector(String director);
    void setAnno(int anno);
    void setDuracion(int duracion);
    void setGenero(Genero genero);
}
