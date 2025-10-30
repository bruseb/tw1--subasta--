package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioOferta {

    void guardarOferta(Oferta oferta);
    List<Oferta> obtenerOfertaPorSubasta(Long idSubasta);
}
