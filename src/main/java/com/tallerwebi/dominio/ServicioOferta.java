package com.tallerwebi.dominio;
import java.util.List;

public interface ServicioOferta {
    Oferta ofertar( Long id, String emailCreador, Float montoOfertado);
    Object[] listarOfertasSubastaJSON(Long idSubasta);
    void verificarFechasSubasta(Long idSubasta);
    List<Subasta> listarSubastasOfertadasPorUsuario(String emailUsuario);
}
