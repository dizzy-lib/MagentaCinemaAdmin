package org.example.magentacinema.infraestructure.implementation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.magentacinema.domain.entities.Genero;
import org.example.magentacinema.domain.entities.Pelicula;
import org.example.magentacinema.domain.entities.PeliculaBase;
import org.example.magentacinema.infraestructure.interfaces.CarteleraRepository;
import org.example.magentacinema.infraestructure.interfaces.DatabaseConnection;
import org.example.magentacinema.infraestructure.interfaces.StoredProcedureExecutor;
import java.sql.SQLException;

/**
 * Este repositorio utiliza el ejecutor de procedimientos almacenados conectado a MySQL
 */
public class CarteleraMySQLRepository implements CarteleraRepository {
    private final StoredProcedureExecutor mySqlStoredProcedureExecutor;

    public CarteleraMySQLRepository(StoredProcedureExecutor mySqlStoredProcedureExecutor) {
        this.mySqlStoredProcedureExecutor = mySqlStoredProcedureExecutor;
    }

    @Override
    public void agregarPelicula(Pelicula pelicula) {
        this.mySqlStoredProcedureExecutor.executeStoredProcedure(
                "sp_agregar_pelicula(?, ?, ?, ?, ?)",
                callableStatement -> {
                    try {
                        callableStatement.setString(1, pelicula.getTitulo());
                        callableStatement.setString(2, pelicula.getDirector());
                        callableStatement.setInt(3, pelicula.getAnno());
                        callableStatement.setInt(4, pelicula.getDuracion());
                        callableStatement.setString(5, pelicula.getGenero().toString());
                    } catch (SQLException e) {
                        throw new RuntimeException("Error configurando parámetros para agregar pelicula", e);
                    }
                });
    }

    public ObservableList<Pelicula> obtenerTodasLasPeliculas() {
        return this.mySqlStoredProcedureExecutor.executeStoredProcedureWithResult(
                "sp_obtener_todas_las_peliculas",
                callableStatement -> {
                    // No hay parámetros para este SP, pero necesitamos el Consumer
                    // Se deja vacío porque el SP no requiere parámetros
                },
                resultSet -> {
                    ObservableList<Pelicula> peliculas = FXCollections.observableArrayList();

                    try {
                        while (resultSet.next()) {
                            // Extraer datos del ResultSet
                            int id = resultSet.getInt("id");
                            String titulo = resultSet.getString("titulo");
                            String director = resultSet.getString("director");
                            int anno = resultSet.getInt("anno");
                            int duracion = resultSet.getInt("duracion");
                            String generoStr = resultSet.getString("genero");

                            // Convertir string a enum Genero
                            Genero genero = Genero.valueOf(generoStr.toUpperCase());

                            // Crear instancia de PeliculaBase
                            Pelicula pelicula = new PeliculaBase(id, titulo, director, anno, duracion, genero);

                            // Agregar a la lista observable
                            peliculas.add(pelicula);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Error al procesar resultados de películas", e);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Error al convertir género o validar datos de película", e);
                    }

                    return peliculas;
                });
    }

    @Override
    public void actualizarPelicula(int id, Pelicula nuevaPelicula) {
        this.mySqlStoredProcedureExecutor.executeStoredProcedure(
                "sp_actualizar_pelicula(?, ?, ?, ?, ?, ?)",
                callableStatement -> {
                    try {
                        callableStatement.setInt(1, id);
                        callableStatement.setString(2, nuevaPelicula.getTitulo());
                        callableStatement.setString(3, nuevaPelicula.getDirector());
                        callableStatement.setInt(4, nuevaPelicula.getAnno());
                        callableStatement.setInt(5, nuevaPelicula.getDuracion());
                        callableStatement.setString(6, nuevaPelicula.getGenero().toString());
                    } catch (SQLException e) {
                        throw new RuntimeException("Error configurando parámetros para agregar pelicula", e);
                    }
                });
    }

    @Override
    public void eliminarPelicula(int id) {
        this.mySqlStoredProcedureExecutor.executeStoredProcedure(
                "sp_eliminar_pelicula(?)",
                callableStatement -> {
                    try {
                        callableStatement.setInt(1, id);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error configurando parámetros para eliminar película", e);
                    }
                }
        );
    }

    public static void main(String[] args) {
        // prueba la conexión a la base de datos
        DatabaseConnection mysqlConnection = new MySQLDatabaseConnection();
        System.out.println("🔎 Estado de la conexión: " + mysqlConnection.testConnection());
    }
}
