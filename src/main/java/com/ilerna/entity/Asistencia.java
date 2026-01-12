package com.ilerna.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad JPA que representa una Asistencia de un cliente a una clase
 */
@Entity
@Table(name = "asistencia")
public class Asistencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    // Relación MANY-TO-ONE con Cliente (EAGER)
    // EAGER: Carga automáticamente el cliente cuando se obtiene la asistencia
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    
    // Relación MANY-TO-ONE con Clase (LAZY)
    // LAZY: Solo carga la clase cuando se accede explícitamente a ella
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase")
    private Clase clase;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Constructor vacío (requerido por JPA)
    public Asistencia() {
    }

    // Constructor con todos los campos
    public Asistencia(Integer id, Cliente cliente, Clase clase, LocalDate fecha) {
        this.id = id;
        this.cliente = cliente;
        this.clase = clase;
        this.fecha = fecha;
    }

    // Constructor sin ID (para inserciones)
    public Asistencia(Cliente cliente, Clase clase, LocalDate fecha) {
        this.cliente = cliente;
        this.clase = clase;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Asistencia{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "null") +
                ", clase=" + (clase != null ? clase.getNombre() : "null") +
                ", fecha=" + fecha +
                '}';
    }
}
