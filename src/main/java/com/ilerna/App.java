package com.ilerna;

import java.sql.*;
import java.util.Scanner;

import com.ilerna.config.DataBaseConnection;
import com.ilerna.controller.GimnasioController;
import com.ilerna.factory.GimnasioControllerFactory;

/**
 * <p>Clase principal de la aplicación<p/>
 * Implementa arquitectura por capas con Clean Architecture
 * 
 * Estructura:
 * - DTO: Objetos de transferencia de datos
 * - DAO: Capa de acceso a datos
 * - Service: Lógica de negocio
 * - Controller: Lógica de presentación
 * - Factory: Creación de objetos complejos
 * - App: Punto de entrada (main)
 */
public class App {
    public static void main(String[] args) {
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
