package com.ilerna.dto;

/**
 * DTO Clase con el conteo de clientes
 */
public class ClaseConConteo {
    private String nombreClase;
    private Integer numeroClientes;

    public ClaseConConteo() {
    }

    public ClaseConConteo(String nombreClase, Integer numeroClientes) {
        this.nombreClase = nombreClase;
        this.numeroClientes = numeroClientes;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public Integer getNumeroClientes() {
        return numeroClientes;
    }

    public void setNumeroClientes(Integer numeroClientes) {
        this.numeroClientes = numeroClientes;
    }

    @Override
    public String toString() {
        return "ClaseConConteo{" +
                "nombreClase='" + nombreClase + '\'' +
                ", numeroClientes=" + numeroClientes +
                '}';
    }
}
