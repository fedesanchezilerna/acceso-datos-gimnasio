package com.ilerna;

import java.sql.*;
import java.util.Scanner;

import com.ilerna.config.DataBaseConnection;
import com.ilerna.config.HibernateUtil;
import com.ilerna.controller.GimnasioController;
import com.ilerna.entity.Cliente;
import com.ilerna.factory.GimnasioControllerFactory;
import com.ilerna.service.AsistenciaHibernateService;
import com.ilerna.service.HibernateEjemploService;
import com.ilerna.service.ObjetoRelacionalService;

import java.time.LocalDate;

/**
 * <p>Clase principal de la aplicación<p/>
 * Implementa arquitectura por capas con Clean Architecture
 * 
 * Estructura:
 * - Entity: Entidades JPA (mapeo ORM con Hibernate)
 * - DTO: Objetos de transferencia de datos
 * - DAO: Capa de acceso a datos
 * - Service: Lógica de negocio
 * - Controller: Lógica de presentación
 * - Factory: Creación de objetos complejos
 * - Config: Configuración (DB, Hibernate)
 * - App: Punto de entrada (main)
 */
public class App {
    public static void main(String[] args) {
        // Demostración de Base de Datos Objeto-Relacional
        ejemploObjetoRelacional();
        
        // Demostración de Hibernate
        // NOTA: Requiere Java 17+ (Hibernate 6.x está compilado con Java 17)
        // Si usas Java 8, comenta esta línea o actualiza a una versión compatible de Hibernate
        // ejemploHibernate();
        
        // Sistema JDBC original
        sistemaJDBC();
    }

    /**
     * Ejemplo de Base de Datos Objeto-Relacional
     * Demuestra tipos compuestos (composite types) de PostgreSQL
     */
    private static void ejemploObjetoRelacional() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║   DEMO: BASE DE DATOS OBJETO-RELACIONAL           ║");
        System.out.println("║   Tipos Compuestos (Composite Types)              ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
        
        try (Connection connection = DataBaseConnection.getConnection()) {
            
            if (connection == null) {
                System.out.println("✗ Error: No se pudo establecer conexión con la base de datos\n");
                return;
            }
            
            ObjetoRelacionalService service = new ObjetoRelacionalService(connection);
            
            // 1. Crear estructura (tipo compuesto + tabla)
            service.crearEstructuraObjetoRelacional();
            
            // 2. Mostrar información del tipo compuesto
            service.mostrarInfoTipoCompuesto();
            
            // 3. Insertar entrenadores usando la notación ROW
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            service.insertarEntrenador("Karloz", "Fuerza");
            service.insertarEntrenador("Ana García", "Yoga");
            service.insertarEntrenador("Miguel Torres", "Cardio");
            service.insertarEntrenador("Laura Martínez", "Pilates");
            service.insertarEntrenador("Pedro Sánchez", "Fuerza");
            
            // 4. Consultar todos los entrenadores
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            service.consultarEntrenadores();
            
            // 5. Consultar por especialidad
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            service.consultarPorEspecialidad("Fuerza");
            
            // 6. Actualizar un entrenador
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            service.actualizarEntrenador(1, "Carlos 'Karloz' Rodríguez", "Fuerza Avanzada");
            
            // 7. Verificar la actualización
            service.consultarEntrenadores();
            
            // 8. Eliminar un entrenador
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            service.eliminarEntrenador(3);
            
            // 9. Consultar estado final
            service.consultarEntrenadores();
            
            // 10. Limpiar estructuras (opcional - comentar si quieres mantener las tablas)
            // service.limpiarEstructuras();
            
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║   FIN DEMO OBJETO-RELACIONAL                     ║");
            System.out.println("╚═══════════════════════════════════════════════════╝\n");
            
        } catch (SQLException e) {
            System.out.println("✗ Error en la demostración objeto-relacional:");
            e.printStackTrace();
            System.out.println();
        }
    }
    
    /**
     * Ejemplo básico de uso de Hibernate
     */
    private static void ejemploHibernate() {
        System.out.println("=== DEMO DE HIBERNATE ===\n");
        
        HibernateEjemploService service = new HibernateEjemploService();
        
        // 1. Insertar un cliente con timestamp para evitar duplicados
        System.out.println("1. Insertando cliente...");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueTimestamp = timestamp.substring(timestamp.length() - 6);
        service.insertarCliente(
            "Cliente Hibernate " + uniqueTimestamp,
                "hibernate" + uniqueTimestamp + "@example.com",
                "999" + uniqueTimestamp
        );
        
        // 2. Consultar cliente por ID
        System.out.println("\n2. Consultando cliente con ID 1...");
        service.consultarClientePorId(1);
        
        // 3. Actualizar cliente (solo si existe)
        System.out.println("\n3. Actualizando email del cliente con ID 1...");
        service.actualizarCliente(1, "cliente.actualizado@example.com");
        
        // Verificar actualización
        System.out.println("\n4. Verificando actualización del cliente con ID 1...");
        service.consultarClientePorId(1);
        
        // 5. DEMO merge(): Insertar cliente nuevo sin ID
        System.out.println("\n5. DEMO merge() - Insertando cliente nuevo (sin ID)...");
        Cliente clienteNuevo = new Cliente(
            "Cliente Merge Nuevo",
            "merge.nuevo" + uniqueTimestamp + "@example.com",
            "888" + uniqueTimestamp
        );
        service.guardarOActualizarCliente(clienteNuevo);
        
        // 6. DEMO merge(): Actualizar cliente existente con ID
        System.out.println("\n6. DEMO merge() - Actualizando cliente existente (con ID)...");
        Cliente clienteExistente = new Cliente(
            1,
            "juan pérez MODIFICADO con merge",
            "cliente.merge.modificado@example.com",
            "123456789"
        );
        service.guardarOActualizarCliente(clienteExistente);
        
        // Verificar la actualización con merge
        System.out.println("\n7. Verificando cliente actualizado con merge...");
        service.consultarClientePorId(1);
        
        // 8. DEMO EAGER vs LAZY Loading
        System.out.println("\n8. DEMO EAGER vs LAZY Loading con Asistencias...");
        AsistenciaHibernateService asistenciaService = new AsistenciaHibernateService();
        
        // Insertar una asistencia de ejemplo
        System.out.println("\n8.1. Insertando asistencia de ejemplo...");
        asistenciaService.insertarAsistencia(1, 1, LocalDate.now());
        
        // Demostrar EAGER loading (problema con sesión cerrada)
        System.out.println("\n8.2. EAGER vs LAZY con sesión cerrada...");
        asistenciaService.demostrarEagerLoading(1);
        
        // Demostrar LAZY loading correcto (dentro de sesión)
        System.out.println("\n8.3. Usando LAZY correctamente (dentro de sesión)...");
        asistenciaService.demostrarLazyLoadingCorrecto(1);
        
        System.out.println("\n=== FIN DEMO HIBERNATE ===\n");
        
        // Cerrar SessionFactory
        HibernateUtil.shutdown();
    }

    /**
     * Sistema original con JDBC
     */
    private static void sistemaJDBC() {
        try (Connection connection = DataBaseConnection.getConnection();
                Scanner scanner = new Scanner(System.in)) {

            if (connection != null) {
                System.out.println("✓ Conexión exitosa a la base de datos\n");

                // Usar factory para crear el controlador con todas las dependencias
                GimnasioController controller = GimnasioControllerFactory.crear(connection, scanner);

                // Ejecutar menú interactivo
                mostrarMenu(controller, scanner);

            } else {
                System.out.println("✗ Error al conectar a la base de datos");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }

    /**
     * Muestra el menú principal y gestiona las opciones del usuario
     */
    private static void mostrarMenu(GimnasioController controller, Scanner scanner) {
        int opcion;

        do {
            System.out.println("SISTEMA DE GESTIÓN DE GIMNASIO");
            System.out.println("1. Mostrar versión de PostgreSQL");
            System.out.println("2. Listar todos los clientes");
            System.out.println("3. Insertar nuevo cliente");
            System.out.println("4. Actualizar información de cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. [DEMO] Transacción: Registrar entrenador + clientes");
            System.out.println("7. [STORED PROC] Insertar entrenador y clase");
            System.out.println("0. Salir\n");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                System.out.println(); // Línea en blanco para mejor legibilidad

                switch (opcion) {
                    case 1:
                        controller.mostrarVersionPostgresSQL();
                        break;
                    case 2:
                        controller.listarClientes();
                        break;
                    case 3:
                        controller.registrarNuevoCliente();
                        break;
                    case 4:
                        controller.actualizarCliente();
                        break;
                    case 5:
                        // TODO: Implementar eliminación
                        break;
                    case 6:
                        controller.registrarEntrenadorConClientesTransaccion();
                        break;
                    case 7:
                        controller.ejecutarProcedimientoInsertarEntrenadorYClase();
                        break;
                    case 0:
                        System.out.println("Ta luego my friend");
                        break;
                    default:
                        System.out.println("✗ Opción no válida. Por favor, intente de nuevo.");
                }

                if (opcion != 0) {
                    System.out.println("\nEnter para continuar...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("✗ Error: Debe ingresar un número válido.");
                scanner.nextLine(); // Limpiar buffer en caso de error
                opcion = -1; // Para que no salga del bucle
            }

        } while (opcion != 0);
    }
}
