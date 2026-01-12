package com.ilerna.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servicio que demuestra el comportamiento de bases de datos objeto-relacionales
 * Simula tipos compuestos (composite types) de PostgreSQL
 * 
 * Características objeto-relacionales:
 * - Tipos de datos definidos por el usuario (UDT - User Defined Types)
 * - Tipos compuestos que agrupan múltiples campos
 * - Tablas que contienen tipos compuestos como columnas
 */
public class ObjetoRelacionalService {
    
    private final Connection connection;

    public ObjetoRelacionalService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Crea la estructura completa de tipos compuestos y tablas
     * Demuestra: CREATE TYPE y tablas con tipos compuestos
     */
    public void crearEstructuraObjetoRelacional() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("=== CREANDO ESTRUCTURA OBJETO-RELACIONAL ===\n");
            
            // 1. Eliminar estructuras existentes (si existen)
            System.out.println("1. Limpiando estructuras anteriores...");
            try {
                stmt.execute("DROP TABLE IF EXISTS entrenador_obj CASCADE;");
                stmt.execute("DROP TYPE IF EXISTS entrenador_tipo CASCADE;");
                System.out.println("   ✓ Estructuras anteriores eliminadas\n");
            } catch (SQLException e) {
                System.out.println("   (No había estructuras previas)\n");
            }
            
            // 2. Crear tipo compuesto entrenador_tipo
            System.out.println("2. Creando tipo compuesto 'entrenador_tipo'...");
            String createType = "CREATE TYPE entrenador_tipo AS (" +
                "    nombre TEXT," +
                "    especialidad TEXT" +
                ");";
            stmt.execute(createType);
            System.out.println("   ✓ Tipo 'entrenador_tipo' creado");
            System.out.println("     - Campo: nombre (TEXT)");
            System.out.println("     - Campo: especialidad (TEXT)\n");
            
            // 3. Crear tabla que usa el tipo compuesto
            System.out.println("3. Creando tabla 'entrenador_obj'...");
            String createTable = "CREATE TABLE entrenador_obj (" +
                "    id SERIAL PRIMARY KEY," +
                "    datos entrenador_tipo" +
                ");";
            stmt.execute(createTable);
            System.out.println("   ✓ Tabla 'entrenador_obj' creada");
            System.out.println("     - id: SERIAL PRIMARY KEY");
            System.out.println("     - datos: entrenador_tipo (tipo compuesto)\n");
            
            System.out.println("✓ Estructura objeto-relacional creada exitosamente\n");
        }
    }

    /**
     * Inserta un entrenador usando la notación ROW
     * Demuestra: INSERT con tipos compuestos usando ROW()
     */
    public void insertarEntrenador(String nombre, String especialidad) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("=== INSERTANDO ENTRENADOR ===");
            System.out.println("Datos a insertar:");
            System.out.println("  - Nombre: " + nombre);
            System.out.println("  - Especialidad: " + especialidad + "\n");
            
            // Escapar comillas simples para evitar SQL injection
            String nombreEscapado = nombre.replace("'", "''");
            String especialidadEscapada = especialidad.replace("'", "''");
            
            // Construir INSERT con ROW() para tipo compuesto
            String insert = String.format(
                "INSERT INTO entrenador_obj (datos) " +
                "SELECT ROW('%s', '%s')::entrenador_tipo;",
                nombreEscapado, especialidadEscapada
            );
            
            int rowsAffected = stmt.executeUpdate(insert);
            System.out.println("✓ Entrenador insertado (" + rowsAffected + " fila afectada)\n");
        }
    }

    /**
     * Consulta todos los entrenadores
     * Demuestra: Acceso a campos de tipos compuestos con notación punto
     */
    public void consultarEntrenadores() throws SQLException {
        String query = "SELECT " +
            "    id, " +
            "    (datos).nombre AS nombre, " +
            "    (datos).especialidad AS especialidad " +
            "FROM entrenador_obj " +
            "ORDER BY id;";
        
        System.out.println("=== CONSULTANDO ENTRENADORES ===");
        System.out.println("Query ejecutada:");
        System.out.println(query);
        System.out.println("Resultados:\n");
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            boolean hayResultados = false;
            System.out.println("┌──────┬─────────────────────┬─────────────────────┐");
            System.out.println("│  ID  │       NOMBRE        │    ESPECIALIDAD     │");
            System.out.println("├──────┼─────────────────────┼─────────────────────┤");
            
            while (rs.next()) {
                hayResultados = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especialidad = rs.getString("especialidad");
                
                System.out.printf("│ %-4d │ %-19s │ %-19s │%n", 
                    id, nombre, especialidad);
            }
            
            System.out.println("└──────┴─────────────────────┴─────────────────────┘\n");
            
            if (!hayResultados) {
                System.out.println("No hay entrenadores registrados.\n");
            }
        }
    }

    /**
     * Actualiza un entrenador por ID
     * Demuestra: UPDATE de tipos compuestos usando ROW()
     */
    public void actualizarEntrenador(int id, String nombre, String especialidad) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("=== ACTUALIZANDO ENTRENADOR ===");
            System.out.println("ID: " + id);
            System.out.println("Nuevos datos:");
            System.out.println("  - Nombre: " + nombre);
            System.out.println("  - Especialidad: " + especialidad + "\n");
            
            // Escapar comillas simples para evitar SQL injection
            String nombreEscapado = nombre.replace("'", "''");
            String especialidadEscapada = especialidad.replace("'", "''");
            
            String update = String.format(
                "UPDATE entrenador_obj " +
                "SET datos = ROW('%s', '%s')::entrenador_tipo " +
                "WHERE id = %d;",
                nombreEscapado, especialidadEscapada, id
            );
            
            int rowsAffected = stmt.executeUpdate(update);
            
            if (rowsAffected > 0) {
                System.out.println("✓ Entrenador actualizado (" + rowsAffected + " fila afectada)\n");
            } else {
                System.out.println("No se encontró ningún entrenador con ID " + id + "\n");
            }
        }
    }

    /**
     * Consulta un entrenador específico por especialidad
     * Demuestra: WHERE con acceso a campos de tipos compuestos
     */
    public void consultarPorEspecialidad(String especialidad) throws SQLException {
        String query = "SELECT " +
            "    id, " +
            "    (datos).nombre AS nombre, " +
            "    (datos).especialidad AS especialidad " +
            "FROM entrenador_obj " +
            "WHERE (datos).especialidad = '" + especialidad + "' " +
            "ORDER BY id;";
        
        System.out.println("=== CONSULTANDO POR ESPECIALIDAD ===");
        System.out.println("Especialidad buscada: " + especialidad + "\n");
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            boolean hayResultados = false;
            System.out.println("Resultados:");
            
            while (rs.next()) {
                hayResultados = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String esp = rs.getString("especialidad");
                
                System.out.printf("  • ID: %d - %s (%s)%n", id, nombre, esp);
            }
            
            if (!hayResultados) {
                System.out.println("  No se encontraron entrenadores con esa especialidad.");
            }
            System.out.println();
        }
    }

    /**
     * Elimina un entrenador por ID
     */
    public void eliminarEntrenador(int id) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            System.out.println("=== ELIMINANDO ENTRENADOR ===");
            System.out.println("ID: " + id + "\n");
            
            String delete = "DELETE FROM entrenador_obj WHERE id = " + id + ";";
            int rowsAffected = stmt.executeUpdate(delete);
            
            if (rowsAffected > 0) {
                System.out.println("✓ Entrenador eliminado (" + rowsAffected + " fila afectada)\n");
            } else {
                System.out.println("⚠ No se encontró ningún entrenador con ID " + id + "\n");
            }
        }
    }

    /**
     * Muestra información sobre el tipo compuesto
     * Consulta el catálogo del sistema de PostgreSQL
     */
    public void mostrarInfoTipoCompuesto() throws SQLException {
        String query = "SELECT " +
            "    t.typname AS tipo, " +
            "    a.attname AS campo, " +
            "    pg_catalog.format_type(a.atttypid, a.atttypmod) AS tipo_dato " +
            "FROM pg_type t " +
            "JOIN pg_attribute a ON a.attrelid = t.typrelid " +
            "WHERE t.typname = 'entrenador_tipo' " +
            "AND a.attnum > 0 " +
            "ORDER BY a.attnum;";
        
        System.out.println("=== INFORMACIÓN DEL TIPO COMPUESTO ===\n");
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("Tipo: entrenador_tipo\n");
            System.out.println("Campos:");
            
            while (rs.next()) {
                String campo = rs.getString("campo");
                String tipoDato = rs.getString("tipo_dato");
                System.out.printf("  • %s: %s%n", campo, tipoDato);
            }
            System.out.println();
        }
    }

    /**
     * Limpia las estructuras objeto-relacionales
     */
    public void limpiarEstructuras() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("=== LIMPIANDO ESTRUCTURAS ===\n");
            stmt.execute("DROP TABLE IF EXISTS entrenador_obj CASCADE;");
            stmt.execute("DROP TYPE IF EXISTS entrenador_tipo CASCADE;");
            System.out.println("✓ Estructuras eliminadas\n");
        }
    }
}
