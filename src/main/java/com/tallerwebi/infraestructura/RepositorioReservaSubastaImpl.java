package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioReservaSubasta;
import com.tallerwebi.dominio.ReservaSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioReservaSubastaImpl implements RepositorioReservaSubasta {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ReservaSubasta buscarRerservaConfirmada(Usuario usuario, Subasta subasta) {
        String hql = "FROM ReservaSubasta r WHERE r.usuario = :usuario AND r.subasta = :subasta AND r.pagoConfirmado = :true";
        List<ReservaSubasta> resultado = sessionFactory.getCurrentSession().createQuery(hql, ReservaSubasta.class)
                .setParameter("usuario", usuario)
                .setParameter("subasta", subasta).getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public void guardar(ReservaSubasta reserva) { sessionFactory.getCurrentSession().save(reserva); }

    @Override
    public void actualizar(ReservaSubasta reserva) { sessionFactory.getCurrentSession().merge(reserva); }

}
