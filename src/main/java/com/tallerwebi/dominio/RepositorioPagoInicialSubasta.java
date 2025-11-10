package com.tallerwebi.dominio;

public interface RepositorioPagoInicialSubasta {

    PagoInicialSubasta buscarPagoInicialConfirmado(Usuario usuario, Subasta subasta);
    void guardar(PagoInicialSubasta reserva);
    void actualizar(PagoInicialSubasta reserva);
}
