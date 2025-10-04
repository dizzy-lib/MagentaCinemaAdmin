package org.example.magentacinema.infraestructure.implementation;

import org.example.magentacinema.core.AppConfig;
import org.example.magentacinema.infraestructure.interfaces.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implementación de conexión a base de datos que utiliza MySQL
 */
public class MySQLDatabaseConnection implements DatabaseConnection {
    private final AppConfig appConfig;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public MySQLDatabaseConnection() {
        // Configura para la lectura de variables de entorno
        this.appConfig = new AppConfig();

        // Carga el driver de MySQL
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e){
            throw new RuntimeException("Error al cargar el driver de MySQL..." + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        String databaseUrl = String.format("jdbc:mysql://%s:%s/%s",
                this.appConfig.getDatabaseHost(),
                this.appConfig.getDatabasePort(),
                this.appConfig.getDatabaseName()
        );

        Connection connection = DriverManager.getConnection(
                databaseUrl,
                this.appConfig.getDatabaseUser(),
                this.appConfig.getDatabasePassword()
        );

        // Deshabilita el autocommit para mejorar la consistencia de las transacciones
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch(SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            return false;
        }
    }
}
