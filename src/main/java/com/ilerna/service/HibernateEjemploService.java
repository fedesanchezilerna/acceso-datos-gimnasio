package com.ilerna.service;

import com.ilerna.config.HibernateUtil;
import com.ilerna.entity.Cliente;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Servicio de ejemplo que demuestra el uso básico de Hibernate
 * con operaciones CRUD sobre la entidad Cliente
 */
public class HibernateEjemploService {

    /**
     * Ejemplo: Insertar un nuevo cliente
     */
    public void insertarCliente(String nombre, String email, String telefono) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Iniciar transacción
            transaction = session.beginTransaction();

            // Crear entidad
            Cliente cliente = new Cliente(nombre, email, telefono);

            // Persistir en la base de datos
            session.persist(cliente);

            // Confirmar transacción
            transaction.commit();
            System.out.println("Cliente insertado: " + cliente);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al insertar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo: Consultar un cliente por ID
     */
    public void consultarClientePorId(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Iniciar transacción
            session.beginTransaction();

            // Consultar por ID
            Cliente cliente = session.find(Cliente.class, id);
            
            if (cliente != null) {
                System.out.println("Cliente encontrado: " + cliente.getNombre());
                System.out.println(cliente);
            } else {
                System.out.println("Cliente con ID " + id + " no encontrado");
            }

            // Confirmar transacción
            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("Error al consultar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo: Actualizar un cliente existente
     */
    public void actualizarCliente(Integer id, String nuevoEmail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener la entidad
            Cliente cliente = session.find(Cliente.class, id);
            
            if (cliente != null) {
                // Modificar la entidad
                cliente.setEmail(nuevoEmail);
                
                // Hibernate detecta automáticamente los cambios y actualiza
                session.merge(cliente);
                
                transaction.commit();
                System.out.println("Cliente actualizado: " + cliente);
            } else {
                System.out.println("Cliente no encontrado");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo: Eliminar un cliente
     */
    public void eliminarCliente(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener la entidad
            Cliente cliente = session.find(Cliente.class, id);
            
            if (cliente != null) {
                // Eliminar
                session.remove(cliente);
                transaction.commit();
                System.out.println("Cliente eliminado: " + id);
            } else {
                System.out.println("Cliente no encontrado");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo: Guardar o actualizar un cliente (merge)
     * Si el cliente tiene ID y existe en BD, actualiza.
     * Si el cliente no tiene ID o no existe, inserta.
     */
    public void guardarOActualizarCliente(Cliente cliente) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // merge() hace INSERT si es nuevo o UPDATE si ya existe
            Cliente clienteGuardado = session.merge(cliente);
            
            transaction.commit();
            
            if (cliente.getId() == null) {
                System.out.println("Cliente INSERTADO con merge: " + clienteGuardado);
            } else {
                System.out.println("Cliente ACTUALIZADO con merge: " + clienteGuardado);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al guardar/actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
