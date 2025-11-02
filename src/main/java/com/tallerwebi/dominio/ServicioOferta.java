package com.tallerwebi.dominio;

public interface ServicioOferta {
    Oferta ofertar( Long id, String emailCreador, Float montoOfertado);
    Object[] listarOfertasSubastaJSON(Long idSubasta);
}
