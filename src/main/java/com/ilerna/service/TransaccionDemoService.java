package com.ilerna.service;

import com.ilerna.dao.ClienteDAO;
import com.ilerna.dao.EntrenadorDAO;
import com.ilerna.dto.Cliente;
import com.ilerna.dto.Entrenador;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que gestiona transacciones complejas
 * Demuestra el uso de transacciones con commit y rollback
 */
public class TransaccionDemoService {
    
    private final Connection connection;
    private final EntrenadorDAO entrenadorDAO;
    private final ClienteDAO clienteDAO;

    public TransaccionDemoService(Connection connection) {
        this.connection = connection;
        this.entrenadorDAO = new EntrenadorDAO(connection);
        this.clienteDAO = new ClienteDAO(connection);
    }

    /**
     * Registra un entrenador y múltiples clientes en una transacción atómica
     * Si alguna operación falla, se hace rollback de todas
     * 
     * @param entrenador Entrenador a insertar
     * @param clientes Lista de clientes a insertar
     * @return Lista con el entrenador y los clientes insertados
     * @throws SQLException Si hay error en la transacción
     */
    public RegistroGrupalResult registrarEntrenadorConClientes(
            Entrenador entrenador, 
            List<Cliente> clientes) throws SQLException {
        
        // Guardar el estado original del autoCommit
        boolean autoCommitOriginal = connection.getAutoCommit();
        
        RegistroGrupalResult resultado = new RegistroGrupalResult();
        
        try {
            // Deshabilitar autoCommit para iniciar la transacción
            connection.setAutoCommit(false);
            
            System.out.println("=== INICIANDO TRANSACCIÓN ===");
            
            // 1. Insertar el entrenador
            System.out.println("1. Insertando entrenador: " + entrenador.getNombre());
            Entrenador entrenadorInsertado = entrenadorDAO.insert(entrenador);
            resultado.setEntrenador(entrenadorInsertado);
            System.out.println("   ✓ Entrenador insertado con ID: " + entrenadorInsertado.getId());
            
            // 2. Insertar cada cliente
            List<Cliente> clientesInsertados = new ArrayList<>();
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                System.out.println((i + 2) + ". Insertando cliente: " + cliente.getNombre());
                
                // Validaciones de negocio
                if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                    throw new IllegalArgumentException("El nombre del cliente es obligatorio");
                }
                if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
                    throw new IllegalArgumentException("El email del cliente es obligatorio");
                }
                
                Cliente clienteInsertado = clienteDAO.insert(cliente);
                clientesInsertados.add(clienteInsertado);
                System.out.println("   ✓ Cliente insertado con ID: " + clienteInsertado.getId());
            }
            
            resultado.setClientes(clientesInsertados);
            
            // Si ha sido exitoso, hacer commit
            connection.commit();
            System.out.println("\n✓ TRANSACCIÓN COMPLETADA EXITOSAMENTE");
            System.out.println("  - 1 entrenador registrado");
            System.out.println("  - " + clientesInsertados.size() + " clientes registrados");
            
            resultado.setExitoso(true);
            
        } catch (SQLException e) {
            // Si hay error SQL, hacer rollback
            System.out.println("\n✗ ERROR EN LA TRANSACCIÓN: " + e.getMessage());
            System.out.println("Ejecutando ROLLBACK...");
            
            try {
                connection.rollback();
                System.out.println("✓ ROLLBACK COMPLETADO - No se guardó ningún registro");
            } catch (SQLException rollbackEx) {
                System.out.println("✗ Error al hacer rollback: " + rollbackEx.getMessage());
                throw rollbackEx;
            }
            
            resultado.setExitoso(false);
            resultado.setMensajeError("Error de base de datos: " + e.getMessage());
            throw e;
            
        } catch (IllegalArgumentException e) {
            // Si hay error de validación, hacer rollback
            System.out.println("\n✗ ERROR DE VALIDACIÓN: " + e.getMessage());
            System.out.println("Ejecutando ROLLBACK...");
            
            try {
                connection.rollback();
                System.out.println("✓ ROLLBACK COMPLETADO - No se guardó ningún registro");
            } catch (SQLException rollbackEx) {
                System.out.println("✗ Error al hacer rollback: " + rollbackEx.getMessage());
                throw rollbackEx;
            }
            
            resultado.setExitoso(false);
            resultado.setMensajeError("Error de validación: " + e.getMessage());
            throw new SQLException(e.getMessage(), e);
            
        } finally {
            // Restaurar el autoCommit original
            try {
                connection.setAutoCommit(autoCommitOriginal);
                System.out.println("=== TRANSACCIÓN FINALIZADA ===\n");
            } catch (SQLException e) {
                System.out.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
        
        return resultado;
    }

    /**
     * Ejecuta el procedimiento almacenado insertar_entrenador_y_clase
     * El procedimiento maneja su propia transacción internamente
     * 
     * @param nombreEntrenador Nombre del entrenador
     * @param especialidad Especialidad del entrenador
     * @param nombreClase Nombre de la clase
     * @param cupoMaximo Cupo máximo de la clase
     * @throws SQLException Si hay error al ejecutar el procedimiento
     */
    public void ejecutarProcedimientoInsertarEntrenadorYClase(
            String nombreEntrenador, 
            String especialidad,
            String nombreClase, 
            int cupoMaximo) throws SQLException {
        
        // Guardar el estado original del autoCommit
        boolean autoCommitOriginal = connection.getAutoCommit();
        
        try {
            // Deshabilitar autoCommit para manejar la transacción manualmente
            connection.setAutoCommit(false);
            
            System.out.println("=== EJECUTANDO PROCEDIMIENTO ALMACENADO ===");
            System.out.println("Entrenador: " + nombreEntrenador + " (" + especialidad + ")");
            System.out.println("Clase: " + nombreClase + " (Cupo: " + cupoMaximo + ")");

            String sql = "CALL insertar_entrenador_y_clase(?, ?, ?, ?)";
            
            try (CallableStatement cs = connection.prepareCall(sql)) {
                // Establecer los parámetros
                cs.setString(1, nombreEntrenador);
                cs.setString(2, especialidad);
                cs.setString(3, nombreClase);
                cs.setInt(4, cupoMaximo);
                
                // Ejecutar el procedimiento
                cs.execute();

                connection.commit();
                System.out.println("Procedimiento ejecutado correctamente");
                
            } catch (SQLException e) {
                System.out.println("Error al ejecutar el procedimiento: " + e.getMessage());
                System.out.println("Ejecutando ROLLBACK...");
                
                try {
                    connection.rollback();
                    System.out.println("ROLLBACK completado");
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
                    throw rollbackEx;
                }
                
                throw e;
            }
            
        } finally {
            // Restaurar el autoCommit original
            try {
                connection.setAutoCommit(autoCommitOriginal);
                System.out.println("=== PROCEDIMIENTO FINALIZADO ===\n");
            } catch (SQLException e) {
                System.out.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
    }

    /**
     * Clase interna para encapsular el resultado de la transacción
     */
    public static class RegistroGrupalResult {
        private boolean exitoso;
        private Entrenador entrenador;
        private List<Cliente> clientes;
        private String mensajeError;

        public boolean isExitoso() {
            return exitoso;
        }

        public void setExitoso(boolean exitoso) {
            this.exitoso = exitoso;
        }

        public Entrenador getEntrenador() {
            return entrenador;
        }

        public void setEntrenador(Entrenador entrenador) {
            this.entrenador = entrenador;
        }

        public List<Cliente> getClientes() {
            return clientes;
        }

        public void setClientes(List<Cliente> clientes) {
            this.clientes = clientes;
        }

        public String getMensajeError() {
            return mensajeError;
        }

        public void setMensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
        }
    }
}
