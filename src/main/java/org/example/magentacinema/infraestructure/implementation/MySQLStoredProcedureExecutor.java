package org.example.magentacinema.infraestructure.implementation;

import org.example.magentacinema.infraestructure.interfaces.DatabaseConnection;
import org.example.magentacinema.infraestructure.interfaces.ResultSetMapper;
import org.example.magentacinema.infraestructure.interfaces.StoredProcedureExecutor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Implementación de ejecutor de procedimientos almacenados para MySQL
 * esta clase realiza la conexión con MYSQL y posee los métodos necesarios para
 * ejecutar procedimientos almacenados dentro de la base de datos
 */
public class MySQLStoredProcedureExecutor implements StoredProcedureExecutor {
    private final DatabaseConnection mysqlDatabaseConnection;

    public MySQLStoredProcedureExecutor(DatabaseConnection mysqlDatabaseConnection) {
        this.mysqlDatabaseConnection = mysqlDatabaseConnection;
    }

    @Override
    public void executeStoredProcedure(String procedureName, Consumer<CallableStatement> parameterSetter) {
        String sql = "{CALL " + procedureName + "}";
        try (Connection connection = mysqlDatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                parameterSetter.accept(callableStatement);
                callableStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error ejecutando el procedimiento almacenado: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión al ejecutar el procedimiento almacenado: " + e.getMessage());
        }
    }

    @Override
    public <T> T executeStoredProcedureWithResult(
            String procedureName,
            Consumer<CallableStatement> parameterSetter,
            ResultSetMapper<T> resultSetMapper
    ) {
        String sql = "{CALL " + procedureName + "}";

        try (Connection connection = this.mysqlDatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            parameterSetter.accept(callableStatement);

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                return resultSetMapper.map(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error ejecutando procedimiento almacenado: " + e.getMessage());
        }
    }
}
