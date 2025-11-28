package com.ilerna.controller;

import com.ilerna.dto.Clase;
import com.ilerna.dto.ClaseConConteo;
import com.ilerna.dto.Cliente;
import com.ilerna.dto.Entrenador;
import com.ilerna.service.AsistenciaService;
import com.ilerna.service.TransaccionDemoService;

import java.util.Optional;
import com.ilerna.service.ClaseService;
import com.ilerna.service.ClienteService;
import com.ilerna.service.DatabaseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controlador principal, maneja la lógica de presentación
 * Intermediario entre la capa de servicio y la interfaz de usuario
 * Responsable de toda la interacción con el usuario (input/output)
 */
public class GimnasioController {

    private final DatabaseService databaseService;
    private final ClienteService clienteService;
    private final ClaseService claseService;
    private final AsistenciaService asistenciaService;
    private final TransaccionDemoService transaccionDemoService;
    private final Scanner scanner;

    public GimnasioController(DatabaseService databaseService,
                              ClienteService clienteService,
                              ClaseService claseService,
                              AsistenciaService asistenciaService,
                              TransaccionDemoService transaccionDemoService,
                              Scanner scanner) {
        this.databaseService = databaseService;
        this.clienteService = clienteService;
        this.claseService = claseService;
        this.asistenciaService = asistenciaService;
        this.transaccionDemoService = transaccionDemoService;
        this.scanner = scanner;
    }

    /**
     * Muestra la versión de PostgreSQL
     */
    public void mostrarVersionPostgresSQL() {
        try {
            String version = databaseService.obtenerVersionPostgreSQL();
            System.out.println("Version PostgresSQL: " + version);
        } catch (SQLException e) {
            System.out.println("Error al obtener la versión de PostgreSQL");
            e.printStackTrace();
        }
    }

    /**
     * Lista todos los clientes del gimnasio
     * Muestra: nombre, email y teléfono
     */
    public void listarClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            for (Cliente cliente : clientes) {
                System.out.println("Nombre: " + cliente.getNombre() +
                                   ", Email: " + cliente.getEmail() +
                                   ", Teléfono: " + cliente.getTelefono());
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes");
            e.printStackTrace();
        }
    }

    /**
     * Solicita datos al usuario y registra un nuevo cliente en la base de datos
     * Maneja toda la interacción con el usuario (input/output)
     */
    public void registrarNuevoCliente() {
        System.out.println("=== REGISTRAR NUEVO CLIENTE ===\n");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        System.out.println(); // Línea en blanco
        
        try {
            // Crear el DTO con los datos del cliente
            Cliente nuevoCliente = new Cliente(nombre, email, telefono);

            // Registrar a través del servicio
            Cliente clienteRegistrado = clienteService.registrarCliente(nuevoCliente);

            System.out.println("✓ Cliente registrado exitosamente:");
            System.out.println("    ID: " + clienteRegistrado.getId());
            System.out.println("    Nombre: " + clienteRegistrado.getNombre());
            System.out.println("    Email: " + clienteRegistrado.getEmail());
            System.out.println("    Teléfono: " + clienteRegistrado.getTelefono());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error de validación: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("✗ Error al registrar el cliente en la base de datos");
            e.printStackTrace();
        }
    }

    /**
     * Solicita datos al usuario y actualiza un cliente existente
     * Muestra primero los clientes disponibles y luego solicita los nuevos datos
     */
    public void actualizarCliente() {
        System.out.println("=== ACTUALIZAR CLIENTE ===\n");
        
        try {
            // Mostrar lista de clientes
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados para actualizar.");
                return;
            }
            
            System.out.println("Clientes disponibles:");
            for (Cliente c : clientes) {
                System.out.println("  ID: " + c.getId() + " - " + c.getNombre() + " (" + c.getEmail() + ")");
            }
            
            System.out.println();
            System.out.print("Ingrese el ID del cliente a actualizar: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            // Buscar el cliente
            Optional<Cliente> clienteOpt = clienteService.buscarClientePorId(id);
            
            if (!clienteOpt.isPresent()) {
                System.out.println("✗ No se encontró un cliente con el ID: " + id);
                return;
            }
            
            Cliente clienteExistente = clienteOpt.get();
            
            System.out.println("\nDatos actuales:");
            System.out.println("  Nombre: " + clienteExistente.getNombre());
            System.out.println("  Email: " + clienteExistente.getEmail());
            System.out.println("  Teléfono: " + clienteExistente.getTelefono());
            
            System.out.println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):\n");
            
            System.out.print("Nuevo nombre [" + clienteExistente.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine();
            if (nuevoNombre.trim().isEmpty()) {
                nuevoNombre = clienteExistente.getNombre();
            }
            
            System.out.print("Nuevo email [" + clienteExistente.getEmail() + "]: ");
            String nuevoEmail = scanner.nextLine();
            if (nuevoEmail.trim().isEmpty()) {
                nuevoEmail = clienteExistente.getEmail();
            }
            
            System.out.print("Nuevo teléfono [" + clienteExistente.getTelefono() + "]: ");
            String nuevoTelefono = scanner.nextLine();
            if (nuevoTelefono.trim().isEmpty()) {
                nuevoTelefono = clienteExistente.getTelefono();
            }
            
            // Crear cliente actualizado
            Cliente clienteActualizado = new Cliente(id, nuevoNombre, nuevoEmail, nuevoTelefono);
            
            // Actualizar a través del servicio
            boolean actualizado = clienteService.actualizarCliente(clienteActualizado);
            
            if (actualizado) {
                System.out.println("\n✓ Cliente actualizado exitosamente:");
                System.out.println("    ID: " + clienteActualizado.getId());
                System.out.println("    Nombre: " + clienteActualizado.getNombre());
                System.out.println("    Email: " + clienteActualizado.getEmail());
                System.out.println("    Teléfono: " + clienteActualizado.getTelefono());
            } else {
                System.out.println("\n✗ No se pudo actualizar el cliente.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error de validación: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("✗ Error al actualizar el cliente en la base de datos");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("✗ Error: Debe ingresar un ID válido.");
            scanner.nextLine(); // Limpiar buffer
        }
    }


    // Métodos adicionales del controlador

    /**
     * Lista las clases de Crossfit o con cupo mayor a 25 alumnos
     */
    public void listarClasesCrossfitOCupoMayor() {
        try {
            List<Clase> clases = claseService.buscarClasesCrossfitOCupoMayor(25);

            if (clases.isEmpty()) {
                System.out.println("No hay clases que cumplan el criterio.");
                return;
            }

            for (Clase clase : clases) {
                System.out.println("Clase: " + clase.getNombre() +
                                   " (Cupo máximo: " + clase.getCupoMaximo() + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las clases");
            e.printStackTrace();
        }
    }

    /**
     * Muestra el reporte de número de clientes por cada clase
     */
    public void mostrarReporteClientesPorClase() {
        try {
            List<ClaseConConteo> reporte = asistenciaService.obtenerReporteClientesPorClase();

            if (reporte.isEmpty()) {
                System.out.println("No hay datos de asistencia.");
                return;
            }

            for (ClaseConConteo claseConteo : reporte) {
                System.out.println("Clase: " + claseConteo.getNombreClase() +
                                   ", Número de clientes: " + claseConteo.getNumeroClientes());
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el reporte de clientes por clase");
            e.printStackTrace();
        }
    }

    /**
     * Demostración de transacciones: registra un entrenador y múltiples clientes
     * Si alguna operación falla, se hace rollback de todas
     */
    public void registrarEntrenadorConClientesTransaccion() {
        System.out.println("=== DEMOSTRACIÓN DE TRANSACCIONES ===");
        System.out.println("Se registrará 1 entrenador + 3 clientes en una transacción atómica\n");
        
        try {
            // Solicitar datos del entrenador
            System.out.println("--- Datos del Entrenador ---");
            System.out.print("Nombre del entrenador: ");
            String nombreEntrenador = scanner.nextLine();
            
            System.out.print("Especialidad: ");
            String especialidad = scanner.nextLine();
            
            Entrenador entrenador = new Entrenador(nombreEntrenador, especialidad);
            
            // Solicitar datos de los clientes
            List<Cliente> clientes = new ArrayList<>();
            System.out.println("\n--- Datos de los Clientes ---");
            
            for (int i = 1; i <= 3; i++) {
                System.out.println("\nCliente " + i + ":");
                System.out.print("  Nombre: ");
                String nombreCliente = scanner.nextLine();
                
                System.out.print("  Email: ");
                String email = scanner.nextLine();
                
                System.out.print("  Teléfono: ");
                String telefono = scanner.nextLine();
                
                clientes.add(new Cliente(nombreCliente, email, telefono));
            }
            
            System.out.println("\n==================================================");

            // Ejecutar la transacción
            transaccionDemoService.registrarEntrenadorConClientes(entrenador, clientes);
            
        } catch (SQLException e) {
            System.out.println("\nLa transacción falló y se revirtieron todos los cambios.");
        } catch (Exception e) {
            System.out.println("✗ Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Ejecuta el procedimiento almacenado insertar_entrenador_y_clase
     * Solicita los datos al usuario y ejecuta el procedimiento
     */
    public void ejecutarProcedimientoInsertarEntrenadorYClase() {
        System.out.println("=== EJECUTAR PROCEDIMIENTO ALMACENADO ===");
        System.out.println("Procedimiento: insertar_entrenador_y_clase\n");
        
        try {
            System.out.println("--- Datos del Entrenador ---");
            System.out.print("Nombre del entrenador: ");
            String nombreEntrenador = scanner.nextLine();
            
            System.out.print("Especialidad: ");
            String especialidad = scanner.nextLine();
            
            System.out.println("\n--- Datos de la Clase ---");
            System.out.print("Nombre de la clase: ");
            String nombreClase = scanner.nextLine();
            
            System.out.print("Cupo máximo: ");
            int cupoMaximo = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            System.out.println("\n==================================================");
            
            // Ejecutar el procedimiento almacenado
            transaccionDemoService.ejecutarProcedimientoInsertarEntrenadorYClase(
                nombreEntrenador, 
                especialidad, 
                nombreClase, 
                cupoMaximo
            );
            
        } catch (SQLException e) {
            System.out.println("\nEl procedimiento falló y se revirtieron todos los cambios.");
        } catch (Exception e) {
            System.out.println("✗ Error inesperado: " + e.getMessage());
            scanner.nextLine(); // Limpiar buffer en caso de error
        }
    }
}
