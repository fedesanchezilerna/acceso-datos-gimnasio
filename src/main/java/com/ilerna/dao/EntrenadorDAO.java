package com.ilerna.dao;

import com.ilerna.dto.Entrenador;
import java.sql.*;

/**
 * Implementación del DAO para Entrenador
 * Gestiona el acceso a datos de la tabla entrenador
 */
public class EntrenadorDAO implements IEntrenadorDAO {
    
    private final Connection connection;

    public EntrenadorDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Entrenador insert(Entrenador entrenador) throws SQLException {
        String sql = "INSERT INTO entrenador (nombre, especialidad) VALUES (?, ?) RETURNING id";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, entrenador.getNombre());
            pstmt.setString(2, entrenador.getEspecialidad());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entrenador.setId(rs.getInt("id"));
                }
            }
        }
        
        return entrenador;
    }
}
