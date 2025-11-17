package com.ilerna.dao;

import com.ilerna.dto.Clase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Clase
 * Gestiona el acceso a datos de la tabla clase
 */
public class ClaseDAO implements IClaseDAO {
    
    private final Connection connection;

    public ClaseDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Clase> getAll() throws SQLException {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT id, nombre, cupo_maximo FROM clase";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clases.add(mapResultSetToClase(rs));
            }
        }
        
        return clases;
    }

    @Override
    public List<Clase> getByNombreOrCupoMayor(String nombre, Integer cupoMinimo) throws SQLException {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT id, nombre, cupo_maximo FROM clase WHERE nombre = ? OR cupo_maximo > ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, cupoMinimo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clases.add(mapResultSetToClase(rs));
                }
            }
        }
        
        return clases;
    }

    /**
     * Mapea un ResultSet a un objeto Clase
     */
    private Clase mapResultSetToClase(ResultSet rs) throws SQLException {
        return new Clase(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getInt("cupo_maximo")
        );
    }
}
