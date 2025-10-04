package org.example.magentacinema.core;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class AppConfig {
    private final Dotenv dotenv;

    public AppConfig() {
        try {
            this.dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .ignoreIfMalformed()      // ignorar líneas malformadas
                    .ignoreIfMissing()        // no fallar si no existe el archivo
                    .load();
        } catch (DotenvException e) {
            throw new RuntimeException("Error cargando archivo .env: " + e.getMessage(), e);
        }

        // Validar variables críticas
        validateRequiredVars();
    }

    private void validateRequiredVars() {
        String[] requiredVars = {
                "DB_HOST", "DB_PORT", "DB_NAME", "DB_USER", "DB_PASSWORD"
        };

        for (String var : requiredVars) {
            if (get(var) == null || get(var).isEmpty()) {
                throw new RuntimeException("Variable de entorno requerida no encontrada: " + var);
            }
        }
    }

    public String get(String key) {
        return dotenv.get(key);
    }

    // Getters específicos
    public String getDatabaseHost() { return get("DB_HOST"); }
    public String getDatabasePort() { return get("DB_PORT"); }
    public String getDatabaseName() { return get("DB_NAME"); }
    public String getDatabaseUser() { return get("DB_USER"); }
    public String getDatabasePassword() { return get("DB_PASSWORD"); }
}