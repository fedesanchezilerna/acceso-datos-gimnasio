-- Consultar por especialidad
SELECT id,
       (datos).nombre AS nombre,
       (datos).especialidad AS especialidad
FROM entrenador_obj
WHERE (datos).especialidad = 'Cerámica'
ORDER BY id;