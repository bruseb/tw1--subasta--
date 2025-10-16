package com.tallerwebi.integracion.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import com.tallerwebi.dominio.RepositorioSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.infraestructura.RepositorioSubastaImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes ={HibernateTestConfig.class})
@Transactional
public class RepositorioSubastaTest {

    @Autowired
    private SessionFactory sessionFactory;


    @Test
    public void deberiaGuardarUnaSubasta(){

        RepositorioSubasta repositorioSubasta = new RepositorioSubastaImpl(sessionFactory);

    Subasta subastaEjemplo= new Subasta();
    subastaEjemplo.setId(1L);
    subastaEjemplo.setTitulo("Monitor");
    subastaEjemplo.setDescripcion("Monitor en buen estado");
    subastaEjemplo.setEstadoProducto("Nuevo");
    subastaEjemplo.setPrecioInicial(10000F);

    repositorioSubasta.guardar(subastaEjemplo);

    String hql = "FROM Subasta WHERE id = :id";
    Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
    query.setParameter("id",1L);


    Subasta subastaObtenida = (Subasta)query.getSingleResult();

    assertThat(subastaEjemplo, is(equalTo(subastaObtenida)));

    }
}