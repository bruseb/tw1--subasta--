package com.tallerwebi.infraestructura;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tallerwebi.dominio.Pago;
import com.tallerwebi.dominio.RepositorioPago;
import org.springframework.stereotype.Repository;

@Repository("repositorioPago")
public class RepositorioPagoTotalImpl implements RepositorioPago {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void guardarPago(Pago pago) {


        entityManager.persist(pago);


    }
}
