package com.ilerna.dao;

import com.ilerna.dto.ClaseConConteo;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para operaciones relacionadas con Asistencia
 */
public interface IAsistenciaDAO {
    
    /**
     * Obtiene el número de clientes por cada clase
     * @return Lista de ClaseConConteo
     * @throws SQLException
     */
    List<ClaseConConteo> getNumeroClientesPorClase() throws SQLException;
}
