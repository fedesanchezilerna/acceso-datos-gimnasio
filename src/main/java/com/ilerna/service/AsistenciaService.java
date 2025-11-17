package com.ilerna.service;

import com.ilerna.dao.IAsistenciaDAO;
import com.ilerna.dto.ClaseConConteo;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar la lógica de negocio relacionada con Asistencias
 */
public class AsistenciaService {
    
    private final IAsistenciaDAO asistenciaDAO;

    public AsistenciaService(IAsistenciaDAO asistenciaDAO) {
        this.asistenciaDAO = asistenciaDAO;
    }

    /**
     * Obtiene el reporte de número de clientes por cada clase
     * @return Lista de ClaseConConteo con la información
     * @throws SQLException
     */
    public List<ClaseConConteo> obtenerReporteClientesPorClase() throws SQLException {
        return asistenciaDAO.getNumeroClientesPorClase();
    }
}
