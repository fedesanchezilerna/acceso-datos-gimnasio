package com.ilerna.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utilidad para gestionar la SessionFactory de Hibernate.
 * Implementa el patrón Singleton para garantizar una única instancia.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Crear la SessionFactory desde hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Error al crear SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Obtiene la instancia única de SessionFactory
     * @return SessionFactory de Hibernate
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory cuando la aplicación termina
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
