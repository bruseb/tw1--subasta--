package com.tallerwebi.dominio;
import java.util.List;

public interface ServicioOferta {
    Oferta ofertar( Long id, String emailCreador, Float montoOfertado);
    Oferta buscarOferta( Long idOferta );
    void eliminarOferta(Oferta oferta, Subasta subasta);

    Object[] listarOfertasSubastaJSON(Long idSubasta);
    void verificarFechasSubasta(Long idSubasta);
    List<Subasta> listarSubastasOfertadasPorUsuario(String emailUsuario);

    Oferta buscarOfertaGanadoraDeSubasta(Long idSubasta);
}
