package com.ilerna.dao;

import com.ilerna.dto.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para las operaciones CRUD de Cliente
 * Siguiendo los principios de Clean Architecture
 */
public interface IClienteDAO {
    
    /**
     * Obtiene todos los clientes
     * @return Lista de clientes
     * @throws SQLException
     */
    List<Cliente> findAll() throws SQLException;
    
    /**
     * Busca un cliente por su ID
     * @param id ID del cliente
     * @return Optional con el cliente si existe
     * @throws SQLException
     */
    Optional<Cliente> getById(Integer id) throws SQLException;
    
    /**
     * Inserta un nuevo cliente
     * @param cliente Cliente a insertar
     * @return Cliente insertado con su ID
     * @throws SQLException
     */
    Cliente insert(Cliente cliente) throws SQLException;
    
    /**
     * Actualiza un cliente existente
     * @param cliente Cliente a actualizar
     * @return true si se actualizó correctamente
     * @throws SQLException
     */
    boolean update(Cliente cliente) throws SQLException;
    
    /**
     * Elimina un cliente por su ID
     * @param id ID del cliente a eliminar
     * @return true si se eliminó correctamente
     * @throws SQLException
     */
    boolean delete(Integer id) throws SQLException;
}
