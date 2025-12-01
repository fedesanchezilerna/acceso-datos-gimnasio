package com.ilerna.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una Clase del gimnasio
 */
@Entity
@Table(name = "clase")
public class Clase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    // Constructor vacío (requerido por JPA)
    public Clase() {
    }

    // Constructor con todos los campos
    public Clase(Integer id, String nombre, Integer cupoMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
    }

    // Constructor sin ID (para inserciones)
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
