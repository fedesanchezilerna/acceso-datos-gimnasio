package com.ilerna.dao;

import com.ilerna.dto.Entrenador;
import java.sql.SQLException;

/**
 * Interfaz DAO para las operaciones de Entrenador
 */
public interface IEntrenadorDAO {
    
    /**
     * Inserta un nuevo entrenador
     * @param entrenador Entrenador a insertar
     * @return Entrenador insertado con su ID
     * @throws SQLException
     */
    Entrenador insert(Entrenador entrenador) throws SQLException;
}
