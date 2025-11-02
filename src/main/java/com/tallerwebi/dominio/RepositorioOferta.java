package com.tallerwebi.dominio;

public interface RepositorioOferta {

    void guardarOferta(Oferta oferta);
    Object[] obtenerOfertasPorSubastaJSON(Long idSubasta);
}
