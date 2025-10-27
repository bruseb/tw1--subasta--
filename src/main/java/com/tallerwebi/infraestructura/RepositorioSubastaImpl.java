package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("repositorioSubasta")
public class RepositorioSubastaImpl implements RepositorioSubasta {

    private SessionFactory sessionFactory;

    public RepositorioSubastaImpl(){}

    @Autowired
    public RepositorioSubastaImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    public void guardar(Subasta subasta) {
        sessionFactory.getCurrentSession().save(subasta);
    }

    public LocalDateTime obtenerTiempoFin(Integer indicador){
        LocalDateTime temp = LocalDateTime.now();
        LocalDateTime result;
        switch(indicador){
            case 0:
                result = temp.plusHours(12);
                break;
            case 1:
                result = temp.plusDays(1);
                break;
            case 2:
                result = temp.plusDays(3);
                break;
            case 3:
                result = temp.plusDays(7);
                break;
            default:
                result = temp;
        }
        return result;
    }

    public Subasta obtenerSubasta(Long id){
        final Session session = sessionFactory.getCurrentSession();
        return (Subasta) session.createCriteria(Subasta.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<Subasta> buscarTodas() {

        String hql = "FROM Subasta";
        return sessionFactory.getCurrentSession().createQuery(hql, Subasta.class).getResultList();
    }

    @Override
    public List<Subasta> buscarSubasta(String titulo) {

        String hql = "FROM Subasta WHERE LOWER(titulo) LIKE :titulo";
        Query<Subasta> query = sessionFactory.getCurrentSession().createQuery(hql,Subasta.class);
        query.setParameter("titulo","%" + titulo.toLowerCase() + "%");
        return query.getResultList();
    }


    @Override
    public List<Subasta> buscarSubastasPorCreador(String emailCreador) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Subasta.class)
                .createAlias("creador", "u")
                .add(Restrictions.eq("u.email", emailCreador))
                .list();
    }

    @Override
    public void actualizar(Subasta subasta) {

        sessionFactory.getCurrentSession().merge(subasta);
        ;
    public boolean existeLaSubasta(String titulo, String descripcion, String estadoProducto , Subcategoria subcategoria, Float precioInicial, Usuario creador) {

        String hql = "SELECT COUNT(s) FROM Subasta s WHERE s.titulo = :titulo AND s.descripcion = :descripcion AND s.estadoProducto = :estadoProducto AND s.subcategoria = :subcategoria AND s.precioInicial = :precioInicial AND s.creador = :creador";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql,Long.class);
        query.setParameter("titulo",titulo.toLowerCase());
        query.setParameter("descripcion",descripcion);
        query.setParameter("estadoProducto",estadoProducto);
        query.setParameter("subcategoria",subcategoria);
        query.setParameter("precioInicial",precioInicial);
        query.setParameter("creador",creador);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<Subasta> buscarSubastasPorCategoriaId(Long idCategoria) {
        String hql = "FROM Subasta s WHERE s.subcategoria.categoria.id = :idCategoria";
        Query<Subasta> query = sessionFactory.getCurrentSession().createQuery(hql,Subasta.class);
        query.setParameter("idCategoria",idCategoria);
        return query.getResultList();
    }

    @Override
    public List<Subasta> buscarSubastasPorSubcategoriaId(Long idSubcategoria) {
        String hql = "FROM Subasta s WHERE s.subcategoria.id = :idSubcategoria";
        Query<Subasta> query = sessionFactory.getCurrentSession().createQuery(hql,Subasta.class);
        query.setParameter("idSubcategoria",idSubcategoria);
        return query.getResultList();
    }

}
