package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.RepositorioNotificacion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioNotificacionImpl implements RepositorioNotificacion {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Notificacion notificacion) {
        sessionFactory.getCurrentSession().save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerNoLeidas(String email) {
        String hql = "FROM Notificacion n WHERE n.usuarioDestino.email = :email AND n.leida = false ORDER BY n.fecha DESC";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Notificacion.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public void marcarTodasComoLeidas(String email) {
        String hql = "UPDATE Notificacion n " +
                "SET n.leida = true " +
                "WHERE n.usuarioDestino.id IN (" +
                "   SELECT u.id FROM Usuario u WHERE u.email = :email" +
                ")";
        sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("email", email)
                .executeUpdate();
    }

    @Override
    public List<Notificacion> obtenerTodas(String email) {
        String hql = "FROM Notificacion n WHERE n.usuarioDestino.email = :email ORDER BY n.fecha DESC";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Notificacion.class)
                .setParameter("email", email)
                .getResultList();
    }

}
