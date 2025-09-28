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
                "DATABASE_URL", "DATABASE_USERNAME", "DATABASE_PASSWORD"
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
    public String getDatabaseUrl() {
        return get("DATABASE_URL");
    }

    public String getDatabaseUsername() {
        return get("DATABASE_USERNAME");
    }

    public String getDatabasePassword() {
        return get("DATABASE_PASSWORD");
    }
}