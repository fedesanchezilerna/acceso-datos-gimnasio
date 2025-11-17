package com.ilerna.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servicio para operaciones generales de base de datos
 */
public class DatabaseService {
    
    private final Connection connection;

    public DatabaseService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Obtiene la versión de PostgreSQL
     * @return Versión de PostgreSQL
     * @throws SQLException
     */
    public String obtenerVersionPostgreSQL() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT version();")) {
            
            if (rs.next()) {
                return rs.getString(1);
            }
            return "Versión desconocida";
        }
    }
}
