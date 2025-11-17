package com.ilerna.dao;

import com.ilerna.dto.Clase;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para las operaciones de Clase
 */
public interface IClaseDAO {
    
    /**
     * Obtiene todas las clases
     * @return Lista de clases
     * @throws SQLException
     */
    List<Clase> getAll() throws SQLException;
    
    /**
     * Busca clases por nombre o cupo máximo
     * @param nombre Nombre de la clase
     * @param cupoMinimo Cupo mínimo
     * @return Lista de clases que coinciden
     * @throws SQLException
     */
    List<Clase> getByNombreOrCupoMayor(String nombre, Integer cupoMinimo) throws SQLException;
}
