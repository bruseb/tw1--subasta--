package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSubcategorias;
import com.tallerwebi.dominio.Subcategoria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioSubcategoriasImpl implements RepositorioSubcategorias {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSubcategoriasImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Subcategoria> listarTodasLasSubcategorias(){
        final var session = sessionFactory.getCurrentSession();
        return session.createQuery("from Subcategoria", Subcategoria.class).list();
    }

    @Override
    public Subcategoria buscarSubcategoriaPorNombreDeRuta(String nombreDeCategoriaEnUrl, String nombreDeSubcategoriaEnUrl) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "select s from Subcategoria s " +
                        "join fetch s.categoria c " +
                        " where c.nombreEnUrl = :nombreCategoria and s.nombreEnUrl = :nombreSubcategoria",
                        Subcategoria.class)
                .setParameter("nombreSubcategoria", nombreDeSubcategoriaEnUrl)
                .setParameter("nombreCategoria", nombreDeCategoriaEnUrl)
                .uniqueResult();
        }

        @Override
        public List<Subcategoria> listarSubcategoriasDeCategoriaSeleccionadaPorId(Long idCategoria) {
            final Session session = sessionFactory.getCurrentSession();
            return session.createQuery(
                            "select s from Subcategoria s " +
                                    "join fetch s.categoria c " +
                                    " where c.id = :idCategoria" +
                                    " order by s.nombre asc",

                    Subcategoria.class)
                    .setParameter("idCategoria", idCategoria)
                    .list();
        }

        @Override
        public List<Subcategoria> listarSubcategoriasPopulares() {
            final Session session = sessionFactory.getCurrentSession();
            List<Subcategoria> lista =
                    session.createQuery("select s " +
                            "from Subcategoria s " +
                            "join fetch s.categoria c " +
                            "order by size(s.subastas) desc", Subcategoria.class)
                    .setMaxResults(10)
                    .getResultList();

            // Inicializa la colección LAZY para evitar LazyInitializationException en la vista
            lista.forEach(s -> Hibernate.initialize(s.getSubastas()));
            return lista;
        }
}



