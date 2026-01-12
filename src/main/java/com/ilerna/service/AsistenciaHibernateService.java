package com.ilerna.service;

import com.ilerna.config.HibernateUtil;
import com.ilerna.entity.Asistencia;
import com.ilerna.entity.Cliente;
import com.ilerna.entity.Clase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

/**
 * Servicio para demostrar EAGER vs LAZY loading en Hibernate
 */
public class AsistenciaHibernateService {

    /**
     * Demostración de EAGER Loading
     * El cliente se carga automáticamente cuando se obtiene la asistencia
     */
    public void demostrarEagerLoading(Integer idAsistencia) {
        System.out.println("\n=== DEMOSTRACIÓN EAGER LOADING ===");
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            // Obtener asistencia
            Asistencia asistencia = session.find(Asistencia.class, idAsistencia);
            
            session.getTransaction().commit();
            // Sesión cerrada aquí
            
            if (asistencia != null) {
                System.out.println("Asistencia ID: " + asistencia.getId());
                System.out.println("Fecha: " + asistencia.getFecha());
                
                // EAGER: El cliente ya está cargado, aunque la sesión esté cerrada
                System.out.println("Cliente (EAGER): " + asistencia.getCliente().getNombre());
                
                // LAZY: La clase NO está cargada, lanzará LazyInitializationException
                try {
                    System.out.println("Clase (LAZY): " + asistencia.getClase().getNombre());
                } catch (Exception e) {
                    System.err.println("ERROR al acceder a Clase (LAZY): " + e.getClass().getSimpleName());
                    System.err.println("   La sesión está cerrada y la clase no fue cargada!");
                }
            } else {
                System.out.println("Asistencia no encontrada");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demostración de LAZY Loading (correcto)
     * Acceder a la clase DENTRO de la sesión
     */
    public void demostrarLazyLoadingCorrecto(Integer idAsistencia) {
        System.out.println("\n=== DEMOSTRACIÓN LAZY LOADING (CORRECTO) ===");
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            // Obtener asistencia
            Asistencia asistencia = session.find(Asistencia.class, idAsistencia);
            
            if (asistencia != null) {
                System.out.println("Asistencia ID: " + asistencia.getId());
                System.out.println("Fecha: " + asistencia.getFecha());
                
                // Acceder al cliente (EAGER) - ya está cargado
                System.out.println("Cliente (EAGER): " + asistencia.getCliente().getNombre());
                
                // Acceder a la clase (LAZY) DENTRO de la sesión - se carga ahora
                System.out.println("Clase (LAZY): " + asistencia.getClase().getNombre());
            } else {
                System.out.println("Asistencia no encontrada");
            }
            
            session.getTransaction().commit();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Insertar una nueva asistencia
     */
    public void insertarAsistencia(Integer idCliente, Integer idClase, LocalDate fecha) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener entidades relacionadas
            Cliente cliente = session.find(Cliente.class, idCliente);
            Clase clase = session.find(Clase.class, idClase);

            if (cliente != null && clase != null) {
                // Crear asistencia
                Asistencia asistencia = new Asistencia(cliente, clase, fecha);
                session.persist(asistencia);
                
                transaction.commit();
                System.out.println("Asistencia registrada: " + asistencia);
            } else {
                System.out.println("Cliente o Clase no encontrado");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al insertar asistencia: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
