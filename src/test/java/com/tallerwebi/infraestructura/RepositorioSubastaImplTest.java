package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.transaction.Transactional;

import java.util.ArrayList;
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
        Categoria cat = new Categoria();
        Subcategoria subcat= new Subcategoria();
        Usuario usuario = new Usuario();
        List<Subcategoria> subcategorias = new ArrayList<>();

        cat.setId(1L);
        cat.setNombre("Piezas de PC");
        cat.setNombreEnUrl("piezaspc");
        sessionFactory.getCurrentSession().save(cat);

        subcat.setId(1L);
        subcat.setNombre("Perifericos");
        subcat.setNombreEnUrl("perifericos");

        subcategorias.add(subcat);
        cat.setSubcategorias(subcategorias);
        sessionFactory.getCurrentSession().save(subcat);

        usuario.setId(1L);
        usuario.setNombre("Usuario");
        usuario.setApellido("Testing");
        usuario.setEmail("test123@gmail.com");
        usuario.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario);

        subasta.setId(1L);
        subasta.setTitulo("Teclado");
        subasta.setDescripcion("Teclado en buen estado");
        subasta.setEstadoProducto("Usado");
        subasta.setSubcategoria(subcat);
        subasta.setCreador(usuario);
        subasta.setPeso(15D);
        subasta.setAlto(15D);
        subasta.setAncho(15D);
        subasta.setLargo(15D);
        subasta.setEstadoSubasta(10);
        subasta.setFechaInicio();
        subasta.setFechaFin(subasta.getFechaInicio().plusHours(1));
        subasta.setPrecioInicial(15000F);

        repositorioSubasta.guardar(subasta);

        String hql = "FROM Subasta WHERE id = :id";
        Query<Subasta> query = sessionFactory.getCurrentSession().createQuery(hql,Subasta.class);
        query.setParameter("id",1L);

        Subasta subastaObtenida = query.getSingleResult();

        assertThat(subastaObtenida,is(equalTo(subasta)));
    }

    @Test
    @Transactional
    public void queSePuedaBuscarUnaSubastaPorId(){

        Subasta subasta = new Subasta();
        Categoria cat = new Categoria();
        Subcategoria subcat= new Subcategoria();
        Usuario usuario = new Usuario();
        List<Subcategoria> subcategorias = new ArrayList<>();

        cat.setId(1L);
        cat.setNombre("Piezas de PC");
        cat.setNombreEnUrl("piezaspc");
        sessionFactory.getCurrentSession().save(cat);

        subcat.setId(1L);
        subcat.setNombre("Perifericos");
        subcat.setNombreEnUrl("perifericos");

        subcategorias.add(subcat);
        cat.setSubcategorias(subcategorias);
        sessionFactory.getCurrentSession().save(subcat);

        usuario.setId(1L);
        usuario.setNombre("Usuario");
        usuario.setApellido("Testing");
        usuario.setEmail("test123@gmail.com");
        usuario.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario);

        subasta.setId(2L);
        subasta.setTitulo("Teclado");
        subasta.setDescripcion("Teclado en buen estado");
        subasta.setEstadoProducto("Usado");
        subasta.setSubcategoria(subcat);
        subasta.setCreador(usuario);
        subasta.setPeso(15D);
        subasta.setAlto(15D);
        subasta.setAncho(15D);
        subasta.setLargo(15D);
        subasta.setEstadoSubasta(10);
        subasta.setFechaInicio();
        subasta.setFechaFin(subasta.getFechaInicio().plusHours(1));
        subasta.setPrecioInicial(15000F);

        repositorioSubasta.guardar(subasta);

        Subasta subastaObtenidaPorId = repositorioSubasta.obtenerSubasta(2L);

        assertThat(subastaObtenidaPorId,is(equalTo(subasta)));
    }

    @Test
    @Transactional
    public void queSePuedaBuscarUnaSubastaPorTitulo(){

        Subasta subasta = new Subasta();
        Categoria cat = new Categoria();
        Subcategoria subcat= new Subcategoria();
        Usuario usuario = new Usuario();
        List<Subcategoria> subcategorias = new ArrayList<>();

        cat.setId(1L);
        cat.setNombre("Piezas de PC");
        cat.setNombreEnUrl("piezaspc");
        sessionFactory.getCurrentSession().save(cat);

        subcat.setId(1L);
        subcat.setNombre("Perifericos");
        subcat.setNombreEnUrl("perifericos");

        subcategorias.add(subcat);
        cat.setSubcategorias(subcategorias);
        sessionFactory.getCurrentSession().save(subcat);

        usuario.setId(1L);
        usuario.setNombre("Usuario");
        usuario.setApellido("Testing");
        usuario.setEmail("test123@gmail.com");
        usuario.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario);

        subasta.setId(3L);
        subasta.setTitulo("Teclado");
        subasta.setDescripcion("Teclado en buen estado");
        subasta.setEstadoProducto("Usado");
        subasta.setSubcategoria(subcat);
        subasta.setCreador(usuario);
        subasta.setPeso(15D);
        subasta.setAlto(15D);
        subasta.setAncho(15D);
        subasta.setLargo(15D);
        subasta.setEstadoSubasta(10);
        subasta.setFechaInicio();
        subasta.setFechaFin(subasta.getFechaInicio().plusHours(1));
        subasta.setPrecioInicial(15000F);

        repositorioSubasta.guardar(subasta);

        List<Subasta> subastaObtenidaPorTitulo = repositorioSubasta.buscarSubasta("teclado");

        assertThat(subastaObtenidaPorTitulo.size(),is(1));
    }

    @Test
    @Transactional
    public void queNoEncuentreSiLaSubastaEstaCerrada(){

        Subasta subasta = new Subasta();
        Categoria cat = new Categoria();
        Subcategoria subcat= new Subcategoria();
        Usuario usuario = new Usuario();
        List<Subcategoria> subcategorias = new ArrayList<>();

        cat.setId(1L);
        cat.setNombre("Piezas de PC");
        cat.setNombreEnUrl("piezaspc");
        sessionFactory.getCurrentSession().save(cat);

        subcat.setId(1L);
        subcat.setNombre("Perifericos");
        subcat.setNombreEnUrl("perifericos");

        subcategorias.add(subcat);
        cat.setSubcategorias(subcategorias);
        sessionFactory.getCurrentSession().save(subcat);

        usuario.setId(1L);
        usuario.setNombre("Usuario");
        usuario.setApellido("Testing");
        usuario.setEmail("test123@gmail.com");
        usuario.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario);

        subasta.setId(4L);
        subasta.setTitulo("Teclado");
        subasta.setDescripcion("Teclado en buen estado");
        subasta.setEstadoProducto("Usado");
        subasta.setSubcategoria(subcat);
        subasta.setCreador(usuario);
        subasta.setPeso(15D);
        subasta.setAlto(15D);
        subasta.setAncho(15D);
        subasta.setLargo(15D);
        subasta.setEstadoSubasta(-1);
        subasta.setFechaInicio();
        subasta.setFechaFin(subasta.getFechaInicio().plusHours(1));
        subasta.setPrecioInicial(15000F);

        repositorioSubasta.guardar(subasta);

        List<Subasta> subastaObtenidaPorTitulo = repositorioSubasta.buscarSubasta("Teclado");

        assertThat(subastaObtenidaPorTitulo.size(),is(0));

    }
}