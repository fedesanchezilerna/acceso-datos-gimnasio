-- Insertar datos válidos
CALL insertar_entrenador_y_clase('Juan Pérez', 'Yoga', 'Yoga Matutino', 20);

-- Intentar insertar con cupo negativo (fallará y hará rollback)
CALL insertar_entrenador_y_clase('Ana Gómez', 'Pilates', 'Pilates Avanzado', -5);