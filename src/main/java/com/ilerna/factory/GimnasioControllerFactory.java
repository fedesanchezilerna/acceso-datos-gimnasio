package com.ilerna.factory;

import com.ilerna.controller.GimnasioController;
import com.ilerna.dao.AsistenciaDAO;
import com.ilerna.dao.ClaseDAO;
import com.ilerna.dao.ClienteDAO;
import com.ilerna.service.AsistenciaService;
import com.ilerna.service.ClaseService;
import com.ilerna.service.ClienteService;
import com.ilerna.service.DatabaseService;
import com.ilerna.service.TransaccionDemoService;

import java.sql.Connection;

/**
 * Factory para crear instancias de GimnasioController con todas sus dependencias
 * Implementa el patrón Factory Method
 */
public class GimnasioControllerFactory {
    
    /**
     * Crea instancia de GimnasioController con todas sus dependencias
     * (Inyección de Dependencias manual)
     * 
     * @param connection Conexión a la base de datos
     * @param scanner Scanner para manejar la entrada del usuario
     * @return GimnasioController configurado y listo para usar
     */
    public static GimnasioController crear(Connection connection, java.util.Scanner scanner) {
        // Capa DAO - Acceso a datos
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        ClaseDAO claseDAO = new ClaseDAO(connection);
        AsistenciaDAO asistenciaDAO = new AsistenciaDAO(connection);
        
        // Capa Service - Lógica de negocio
        DatabaseService databaseService = new DatabaseService(connection);
        ClienteService clienteService = new ClienteService(clienteDAO);
        ClaseService claseService = new ClaseService(claseDAO);
        AsistenciaService asistenciaService = new AsistenciaService(asistenciaDAO);
        TransaccionDemoService transaccionDemoService = new TransaccionDemoService(connection);
        
        // Capa Controller - Presentación
        return new GimnasioController(
            databaseService,
            clienteService,
            claseService,
            asistenciaService,
                transaccionDemoService,
            scanner
        );
    }
}
