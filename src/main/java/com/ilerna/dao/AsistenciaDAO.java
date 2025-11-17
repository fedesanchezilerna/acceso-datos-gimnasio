package com.ilerna.dao;

import com.ilerna.dto.ClaseConConteo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Asistencia
 * Gestiona las consultas relacionadas con la tabla asistencia
 */
public class AsistenciaDAO implements IAsistenciaDAO {
    
    private final Connection connection;

    public AsistenciaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ClaseConConteo> getNumeroClientesPorClase() throws SQLException {
        List<ClaseConConteo> resultado = new ArrayList<>();
        String sql = "SELECT c.nombre AS clase, COUNT(a.id_cliente) AS numero_clientes " +
                     "FROM clase c " +
                     "LEFT JOIN asistencia a ON c.id = a.id_clase " +
                     "GROUP BY c.nombre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ClaseConConteo claseConteo = new ClaseConConteo(
                    rs.getString("clase"),
                    rs.getInt("numero_clientes")
                );
                resultado.add(claseConteo);
            }
        }
        
        return resultado;
    }
}
