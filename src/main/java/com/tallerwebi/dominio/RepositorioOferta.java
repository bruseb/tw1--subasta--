package com.tallerwebi.dominio;

public interface RepositorioOferta {

    void guardarOferta(Oferta oferta);
    Object[] obtenerOfertaPorSubasta(Long idSubasta);
}
