-- ========================================
-- DEMOSTRACIÓN: BASE DE DATOS OBJETO-RELACIONAL
-- Tipos Compuestos (Composite Types) en PostgreSQL
-- ========================================

-- Limpiar estructuras existentes (si existen)
DROP TABLE IF EXISTS entrenador_obj CASCADE;
DROP TYPE IF EXISTS entrenador_tipo CASCADE;

-- 1. CREAR TIPO COMPUESTO
CREATE TYPE entrenador_tipo AS (
    nombre TEXT,
    especialidad TEXT
);

-- Verificar la creación del tipo
SELECT 
    t.typname AS tipo,
    a.attname AS campo,
    pg_catalog.format_type(a.atttypid, a.atttypmod) AS tipo_dato
FROM pg_type t
JOIN pg_attribute a ON a.attrelid = t.typrelid
WHERE t.typname = 'entrenador_tipo'
AND a.attnum > 0
ORDER BY a.attnum;

-- 2. CREAR TABLA CON TIPO COMPUESTO
CREATE TABLE entrenador_obj (
    id SERIAL PRIMARY KEY,
    datos entrenador_tipo
);

-- 3. INSERTAR DATOS USANDO ROW()

-- Inserción básica con ROW()
INSERT INTO entrenador_obj (datos) 
SELECT ROW('Karloz', 'Fuerza')::entrenador_tipo;

-- Más inserciones
INSERT INTO entrenador_obj (datos) 
SELECT ROW('Ana García', 'Yoga')::entrenador_tipo;

INSERT INTO entrenador_obj (datos) 
SELECT ROW('Miguel Torres', 'Cardio')::entrenador_tipo;

INSERT INTO entrenador_obj (datos) 
SELECT ROW('Laura Martínez', 'Pilates')::entrenador_tipo;

INSERT INTO entrenador_obj (datos) 
SELECT ROW('Pedro Sánchez', 'Fuerza')::entrenador_tipo;

-- 4. CONSULTAR DATOS

-- Consulta básica - acceso a campos con notación punto
-- IMPORTANTE: Los paréntesis alrededor de (datos) son obligatorios
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
ORDER BY id;

-- 5. FILTRAR POR CAMPO DEL TIPO COMPUESTO

-- Buscar por especialidad
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
WHERE (datos).especialidad = 'Fuerza';

-- Buscar por nombre (usando LIKE)
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
WHERE (datos).nombre LIKE 'A%';

-- 6. ACTUALIZAR DATOS

-- Actualizar un entrenador completo
UPDATE entrenador_obj 
SET datos = ROW('Carlos "Karloz" Rodríguez', 'Fuerza Avanzada')::entrenador_tipo 
WHERE id = 1;

-- Verificar la actualización
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
WHERE id = 1;

-- 7. CONSULTAS AVANZADAS

-- Ordenar por especialidad y luego por nombre
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
ORDER BY (datos).especialidad, (datos).nombre;

-- Contar entrenadores por especialidad
SELECT 
    (datos).especialidad AS especialidad,
    COUNT(*) AS total_entrenadores
FROM entrenador_obj
GROUP BY (datos).especialidad
ORDER BY total_entrenadores DESC;

-- Subconsulta: Encontrar especialidades únicas
SELECT DISTINCT (datos).especialidad AS especialidad
FROM entrenador_obj
ORDER BY especialidad;

-- 8. CREAR ÍNDICE SOBRE CAMPO DEL TIPO COMPUESTO

-- No se puede indexar directamente el tipo completo, pero sí campos individuales
CREATE INDEX idx_especialidad ON entrenador_obj(((datos).especialidad));

-- Verificar que el índice se usa en consultas
EXPLAIN ANALYZE
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad
FROM entrenador_obj
WHERE (datos).especialidad = 'Fuerza';

-- 9. ELIMINACIÓN DE DATOS

-- Eliminar un entrenador específico
DELETE FROM entrenador_obj WHERE id = 3;

-- Verificar la eliminación
SELECT COUNT(*) AS total_entrenadores FROM entrenador_obj;

-- 10. CONSULTAR EL TIPO COMPLETO (sin descomponer)

-- Ver el tipo compuesto completo
SELECT id, datos
FROM entrenador_obj;

-- Convertir el tipo a JSON para mejor visualización
SELECT 
    id,
    row_to_json(datos) AS entrenador_json
FROM entrenador_obj;

-- EJEMPLO AVANZADO: MÚLTIPLES TIPOS COMPUESTOS

-- Crear tipo para contacto
CREATE TYPE contacto_tipo AS (
    email TEXT,
    telefono TEXT
);

-- Crear tabla que usa múltiples tipos compuestos
CREATE TABLE entrenador_completo (
    id SERIAL PRIMARY KEY,
    datos entrenador_tipo,
    contacto contacto_tipo
);

-- Insertar con múltiples tipos compuestos
INSERT INTO entrenador_completo (datos, contacto)
SELECT 
    ROW('Juan Pérez', 'Natación')::entrenador_tipo,
    ROW('juan@gym.com', '555-1234')::contacto_tipo;

-- Consultar múltiples tipos compuestos
SELECT 
    id,
    (datos).nombre AS nombre,
    (datos).especialidad AS especialidad,
    (contacto).email AS email,
    (contacto).telefono AS telefono
FROM entrenador_completo;

-- LIMPIEZA (opcional - comentar si quieres mantener las tablas)

-- DROP TABLE IF EXISTS entrenador_completo CASCADE;
-- DROP TABLE IF EXISTS entrenador_obj CASCADE;
-- DROP TYPE IF EXISTS contacto_tipo CASCADE;
-- DROP TYPE IF EXISTS entrenador_tipo CASCADE;

-- ========================================
-- NOTAS IMPORTANTES
-- ========================================

/*
1. NOTACIÓN DE ACCESO:
   - Siempre usar paréntesis: (datos).nombre
   - Sin paréntesis dará error de sintaxis

2. LIMITACIONES:
   - No se pueden definir claves foráneas sobre tipos compuestos
   - Los índices deben crearse sobre campos individuales con triple paréntesis
   - Algunos ORMs tienen soporte limitado

3. VENTAJAS:
   - Agrupa datos relacionados lógicamente
   - Reutilizable en múltiples tablas
   - Facilita el mantenimiento
   - Mejor semántica del modelo

4. CUÁNDO USAR:
   ✓ Datos que siempre van juntos (dirección, coordenadas, contacto)
   ✓ No se consultan campos de forma independiente frecuentemente
   ✓ Se necesita reutilizar la estructura en múltiples tablas
   
   ✗ Evitar si necesitas índices complejos
   ✗ Evitar si los campos necesitan relaciones con otras tablas
   ✗ Evitar si se requiere alta normalización

5. SINTAXIS ALTERNATIVAS PARA INSERT:
   - INSERT ... SELECT ROW(...)::tipo  (recomendada)
   - INSERT ... VALUES (ROW(...))
   - INSERT ... VALUES (('valor1', 'valor2')::tipo)
*/

-- ========================================
-- FIN DEL SCRIPT
-- ========================================

SELECT 'Script ejecutado exitosamente' AS resultado;
