package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RepositorioSubastaImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioSubastaImpl repositorioSubasta;

    @BeforeEach
    public void init(){
        this.repositorioSubasta = new RepositorioSubastaImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void deberiaGuardaUnaSubasta(){

        Subasta subasta = new Subasta();
        subasta.setId(1L);
        subasta.setTitulo("Teclado");
        subasta.setDescripcion("Teclado en buen estado");
        subasta.setEstadoProducto("usado");
        subasta.setPrecioInicial(15000F);

        repositorioSubasta.guardar(subasta);

        String hql = "FROM Subasta WHERE id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id",1L);

        Subasta subastaObtenida = (Subasta)query.getSingleResult();

        assertThat(subastaObtenida,is(equalTo(subasta)));

    }

    @Test
    @Transactional
    public void queSePuedaBuscarUnaSubastaPorId(){

        Subasta subasta = new Subasta();
        subasta.setId(1L);
        subasta.setTitulo("Monitor");
        subasta.setDescripcion("Monitor en buen estado");
        subasta.setEstadoProducto("nuevo");
        subasta.setPrecioInicial(12000F);

        repositorioSubasta.guardar(subasta);

        Subasta subastaObtenidaPorId = repositorioSubasta.obtenerSubasta(1L);

        assertThat(subastaObtenidaPorId,is(equalTo(subasta)));


    }

    @Test
    @Transactional
    public void queSePuedaBuscarUnaSubastaPorTitulo(){

        Subasta subasta = new Subasta();
        subasta.setId(3L);
        subasta.setTitulo("Mouse");
        subasta.setDescripcion("Mouse en buen estado");
        subasta.setEstadoProducto("nuevo");
        subasta.setPrecioInicial(10000F);

        repositorioSubasta.guardar(subasta);

        List<Subasta> subastaObtenidaPorTitulo = repositorioSubasta.buscarSubasta("mouse");

        assertThat(subastaObtenidaPorTitulo.size(),is(1));


    }

    @Test
    @Transactional
    public void queSePuedaBuscarMasDeUnaSubastaPorTitulo(){

        Subasta subasta1 = new Subasta();
        subasta1.setId(4L);
        subasta1.setTitulo("El mejor Mouse");
        subasta1.setDescripcion("Mouse en buen estado");
        subasta1.setEstadoProducto("nuevo");
        subasta1.setPrecioInicial(9000F);

        repositorioSubasta.guardar(subasta1);

        Subasta subasta2 = new Subasta();
        subasta2.setId(5L);
        subasta2.setTitulo("Mouse gammer");
        subasta2.setDescripcion("Mouse con poco uso");
        subasta2.setEstadoProducto("usado");
        subasta2.setPrecioInicial(12000F);

        repositorioSubasta.guardar(subasta2);

        List<Subasta> subastaObtenidaPorTitulo = repositorioSubasta.buscarSubasta("mouse");

        assertThat(subastaObtenidaPorTitulo.size(),is(2));


    }
}