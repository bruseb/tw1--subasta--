package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioPagoInicialSubasta;
import com.tallerwebi.dominio.PagoInicialSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class RepositorioPagoInicialSubastaImpl implements RepositorioPagoInicialSubasta {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPagoInicialSubastaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public PagoInicialSubasta buscarPagoInicialConfirmado(Usuario usuario, Subasta subasta) {
        String hql = "FROM PagoInicialSubasta p WHERE p.usuario = :usuario AND p.subasta = :subasta AND p.pagoConfirmado = true";
        List<PagoInicialSubasta> resultado = sessionFactory.getCurrentSession().createQuery(hql, PagoInicialSubasta.class)
                .setParameter("usuario", usuario)
                .setParameter("subasta", subasta)
                .getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public void guardar(PagoInicialSubasta reserva) { sessionFactory.getCurrentSession().save(reserva); }

    @Override
    public void actualizar(PagoInicialSubasta reserva) { sessionFactory.getCurrentSession().merge(reserva); }

}
