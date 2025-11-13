package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.Pago;
import com.tallerwebi.dominio.RepositorioPago;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPago")
public class RepositorioPagoTotalImpl implements RepositorioPago {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPagoTotalImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void guardarPago(Pago pago) {


        sessionFactory.getCurrentSession().save(pago);


    }
}
