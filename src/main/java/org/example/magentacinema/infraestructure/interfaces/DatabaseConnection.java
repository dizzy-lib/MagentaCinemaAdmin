package org.example.magentacinema.infraestructure.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
    /**
     * Obtiene la conexión a la base de datos
     * para poder hacer consultas y transacciónes
     * @return conexión a la base de datos
     */
    Connection getConnection() throws SQLException;

    /**
     * Prueba la conexión a la base de datos
     * @return flag indicativo del resultado de la conexión
     */
    boolean testConnection();
}
