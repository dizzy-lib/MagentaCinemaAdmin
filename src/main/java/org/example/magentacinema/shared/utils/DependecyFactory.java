package org.example.magentacinema.shared.utils;

import org.example.magentacinema.domain.services.PeliculaService;
import org.example.magentacinema.infraestructure.implementation.CarteleraMySQLRepository;
import org.example.magentacinema.infraestructure.implementation.MySQLDatabaseConnection;
import org.example.magentacinema.infraestructure.implementation.MySQLStoredProcedureExecutor;
import org.example.magentacinema.infraestructure.interfaces.CarteleraRepository;
import org.example.magentacinema.infraestructure.interfaces.DatabaseConnection;
import org.example.magentacinema.infraestructure.interfaces.StoredProcedureExecutor;

/**
 * Clase de utilidad que funciona bien para la generación de servicios utilizando
 * inyección de dependencias, si se necesitan agregar nuevos módulos de inyección
 * de dependencias se pueden agregar aqui.
 */
public class DependecyFactory {
    /**
     * Clase estática para poder utilizar sin crear nueva instancia de la
     * clase principal.
     * @return servicio de película
     */
    public static PeliculaService createPeliculaService() {

        // Llama a todas las dependencias del servicio de películas
        DatabaseConnection databaseConnection = new MySQLDatabaseConnection();
        StoredProcedureExecutor storedProcedureExecutor = new MySQLStoredProcedureExecutor(databaseConnection);
        CarteleraRepository carteleraRepository = new CarteleraMySQLRepository(storedProcedureExecutor);

        return new PeliculaService(carteleraRepository);
    }
}
