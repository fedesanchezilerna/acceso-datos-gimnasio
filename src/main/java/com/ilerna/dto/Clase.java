package com.ilerna.dto;

/**
 * DTO Clase del gimnasio
 */
public class Clase {
    private Integer id;
    private String nombre;
    private Integer cupoMaximo;

    // Constructor vacío
    public Clase() {
    }

    // Constructor con todos los campos
    public Clase(Integer id, String nombre, Integer cupoMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
    }

    // Constructor sin ID > Inserciones
    public Clase(String nombre, Integer cupoMaximo) {
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    @Override
    public String toString() {
        return "Clase{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cupoMaximo=" + cupoMaximo +
                '}';
    }
}
