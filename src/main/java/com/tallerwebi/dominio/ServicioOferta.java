package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioOferta {
    Oferta ofertar( Long id, String emailCreador, Float montoOfertado);
    List<Oferta> listarOfertasSubasta(Long idSubasta);
}
