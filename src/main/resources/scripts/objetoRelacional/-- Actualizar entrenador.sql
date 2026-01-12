-- Actualizar entrenador
UPDATE entrenador_obj
SET datos = ROW('Carlos ''Karloz'' Rodríguez', 'Cerámica')::entrenador_tipo
WHERE id = 1;