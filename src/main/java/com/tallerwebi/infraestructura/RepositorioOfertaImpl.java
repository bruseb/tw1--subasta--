package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Oferta;
import com.tallerwebi.dominio.RepositorioOferta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioOferta")
public class RepositorioOfertaImpl implements RepositorioOferta {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioOfertaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarOferta(Oferta oferta) {
        sessionFactory.getCurrentSession().save(oferta);
    }

    @Override
    public Object[] obtenerOfertasPorSubastaJSON(Long idSubasta) {
        String hql = "SELECT o.id, o.fechaOferta, o.montoOfertado, o.ofertadorID.id, o.ofertadorID.nombre, o.ofertadorID.apellido, o.ofertadorID.email " +
                     "FROM Oferta o " +
                     "WHERE o.subasta.id = :idSubasta";
        Query<Object[]> query = sessionFactory.getCurrentSession().createQuery(hql,Object[].class);
        query.setParameter("idSubasta",idSubasta);

        return new List[]{query.getResultList()};
    }

    @Override
    public List<Subasta> obtenerSubastasOfertadasPorUsuario(String emailUsuario) {
        String hql = "SELECT DISTINCT o.subasta " +
                "FROM Oferta o " +
                "WHERE o.ofertadorID.email = :email AND o.subasta.estadoSubasta = :estadoSubasta";
        Query<Subasta> query = sessionFactory.getCurrentSession().createQuery(hql, Subasta.class);
        query.setParameter("email", emailUsuario);
        query.setParameter("estadoSubasta",10);
        return query.getResultList();
    }

    @Override
    public List<Usuario> obtenerOfertantesPorSubasta(Subasta subasta, Usuario excluido) {
        String hql = "SELECT DISTINCT o.ofertadorID FROM Oferta o WHERE o.subasta = :subasta AND o.ofertadorID != :excluido";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Usuario.class)
                .setParameter("subasta", subasta)
                .setParameter("excluido", excluido)
                .getResultList();
    }

    @Override
    public Oferta obtenerOferta(Long idOferta) {
        String hql = "FROM Oferta o WHERE o.id = :idOferta";
        Query<Oferta> query = sessionFactory.getCurrentSession().createQuery(hql, Oferta.class);
        query.setParameter("idOferta",idOferta);
        return query.getSingleResult();
    }

    @Override
    public void eliminarOferta(Oferta oferta){
        sessionFactory.getCurrentSession().delete(oferta);
    }

    @Override
    public Float obtenerOfertaMaxima(Long idSubasta){
        String hql = "SELECT MAX(o.montoOfertado) FROM Oferta o WHERE o.subasta.id = :idSubasta";
        Query<Float> query = sessionFactory.getCurrentSession().createQuery(hql,Float.class);
        query.setParameter("idSubasta",idSubasta);

        return  query.uniqueResult();
    }
}
