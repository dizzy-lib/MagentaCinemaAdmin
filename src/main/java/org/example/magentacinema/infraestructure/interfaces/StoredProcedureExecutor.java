package org.example.magentacinema.infraestructure.interfaces;

import java.sql.CallableStatement;
import java.util.function.Consumer;

/**
 * Interfaz para ejecutores de procedimientos almacenados
 * estos procedimientos pueden ser implementados con diferentes motores de base de datos
 */
public interface StoredProcedureExecutor {
    void executeStoredProcedure(String procedureName, Consumer<CallableStatement> parameterSetter);
    <T> T executeStoredProcedureWithResult(
            String procedureName,
            Consumer<CallableStatement> parameterSetter,
            ResultSetMapper<T> resultSetMapper
    );
}
