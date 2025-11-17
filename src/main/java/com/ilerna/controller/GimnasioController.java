package com.ilerna.controller;

import com.ilerna.dto.Clase;
import com.ilerna.dto.ClaseConConteo;
import com.ilerna.dto.Cliente;
import com.ilerna.service.AsistenciaService;
import com.ilerna.service.ClaseService;
import com.ilerna.service.ClienteService;
import com.ilerna.service.DatabaseService;

import java.sql.SQLException;
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
    private final Scanner scanner;

    public GimnasioController(DatabaseService databaseService,
                              ClienteService clienteService,
                              ClaseService claseService,
                              AsistenciaService asistenciaService,
                              Scanner scanner) {
        this.databaseService = databaseService;
        this.clienteService = clienteService;
        this.claseService = claseService;
        this.asistenciaService = asistenciaService;
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
}
