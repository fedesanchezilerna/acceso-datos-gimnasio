package com.ilerna.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un Entrenador del gimnasio
 */
@Entity
@Table(name = "entrenador")
public class Entrenador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "especialidad", length = 100)
    private String especialidad;

    // Constructor vacío (requerido por JPA)
    public Entrenador() {
    }

    // Constructor con todos los campos
    public Entrenador(Integer id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Constructor sin ID (para inserciones)
    public Entrenador(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Entrenador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}
