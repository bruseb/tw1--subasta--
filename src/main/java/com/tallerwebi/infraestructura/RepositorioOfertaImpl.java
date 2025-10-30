package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Oferta;
import com.tallerwebi.dominio.RepositorioOferta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
    public List<Oferta> obtenerOfertaPorSubasta(Long idSubasta) {
        //final Session session = sessionFactory.getCurrentSession();
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
        }catch(Exception e){
            session = sessionFactory.openSession();
        }
        return session.createCriteria(Oferta.class)
                .add(Restrictions.eq("subasta.id", idSubasta))
                .list();
    }
}
