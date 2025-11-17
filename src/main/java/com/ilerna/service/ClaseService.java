package com.ilerna.service;

import com.ilerna.dao.IClaseDAO;
import com.ilerna.dto.Clase;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar la lógica de negocio relacionada con Clases
 */
public class ClaseService {
    
    private final IClaseDAO claseDAO;

    public ClaseService(IClaseDAO claseDAO) {
        this.claseDAO = claseDAO;
    }

    /**
     * Obtiene todas las clases disponibles
     * @return Lista de clases
     * @throws SQLException
     */
    public List<Clase> obtenerTodasLasClases() throws SQLException {
        return claseDAO.getAll();
    }

    /**
     * Busca clases de Crossfit o con cupo mayor al especificado
     * @param cupoMinimo Cupo mínimo a buscar
     * @return Lista de clases que cumplen el criterio
     * @throws SQLException
     */
    public List<Clase> buscarClasesCrossfitOCupoMayor(Integer cupoMinimo) throws SQLException {
        return claseDAO.getByNombreOrCupoMayor("crossfit", cupoMinimo);
    }
}
