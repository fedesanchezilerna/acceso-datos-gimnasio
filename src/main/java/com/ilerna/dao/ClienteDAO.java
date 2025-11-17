package com.ilerna.dao;

import com.ilerna.dto.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del DAO para Cliente
 * Gestiona el acceso a datos de la tabla cliente
 */
public class ClienteDAO implements IClienteDAO {
    
    private final Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, email, telefono FROM cliente";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        }
        
        return clientes;
    }

    @Override
    public Optional<Cliente> getById(Integer id) throws SQLException {
        String sql = "SELECT id, nombre, email, telefono FROM cliente WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCliente(rs));
                }
            }
        }
        
        return Optional.empty();
    }

    @Override
    public Cliente insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, email, telefono) VALUES (?, ?, ?) RETURNING id";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getTelefono());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt("id"));
                }
            }
        }
        
        return cliente;
    }

    @Override
    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, email = ?, telefono = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setInt(4, cliente.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Cliente
     */
    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("telefono")
        );
    }
}
