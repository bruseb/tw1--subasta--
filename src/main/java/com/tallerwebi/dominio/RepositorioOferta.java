package com.tallerwebi.dominio;
import java.util.List;

public interface RepositorioOferta {
    void guardarOferta(Oferta oferta);
    void eliminarOferta(Oferta oferta);
    Float obtenerOfertaMaxima(Long idSubasta);
    Oferta obtenerOferta(Long idOferta);
    Object[] obtenerOfertasPorSubastaJSON(Long idSubasta);
    List<Subasta> obtenerSubastasOfertadasPorUsuario(String emailUsuario);
    List<Usuario> obtenerOfertantesPorSubasta(Subasta subasta, Usuario usuario);
}
