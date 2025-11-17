package com.ilerna.service;

import com.ilerna.dao.IClienteDAO;
import com.ilerna.dto.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio relacionada con Clientes
 * Capa intermedia entre la presentación y el acceso a datos
 */
public class ClienteService {
    
    private final IClienteDAO clienteDAO;

    public ClienteService(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /**
     * Obtiene todos los clientes del gimnasio
     * @return Lista de clientes
     * @throws SQLException
     */
    public List<Cliente> obtenerTodosLosClientes() throws SQLException {
        return clienteDAO.findAll();
    }

    /**
     * Busca un cliente por su ID
     * @param id ID del cliente
     * @return Optional con el cliente si existe
     * @throws SQLException
     */
    public Optional<Cliente> buscarClientePorId(Integer id) throws SQLException {
        return clienteDAO.getById(id);
    }

    /**
     * Registra un nuevo cliente
     * @param cliente Cliente a registrar
     * @return Cliente registrado con su ID
     * @throws SQLException
     */
    public Cliente registrarCliente(Cliente cliente) throws SQLException {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente es obligatorio");
        }
        
        return clienteDAO.insert(cliente);
    }

    /**
     * Actualiza los datos de un cliente
     * @param cliente Cliente con los datos actualizados
     * @return true si se actualizó correctamente
     * @throws SQLException
     */
    public boolean actualizarCliente(Cliente cliente) throws SQLException {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio para actualizar");
        }
        return clienteDAO.update(cliente);
    }

    /**
     * Elimina un cliente
     * @param id ID del cliente a eliminar
     * @return true si se eliminó correctamente
     * @throws SQLException
     */
    public boolean eliminarCliente(Integer id) throws SQLException {
        return clienteDAO.delete(id);
    }
}
