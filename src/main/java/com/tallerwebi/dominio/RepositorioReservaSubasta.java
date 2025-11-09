package com.tallerwebi.dominio;

public interface RepositorioReservaSubasta {

    public ReservaSubasta buscarRerservaConfirmada(Usuario usuario, Subasta subasta);
    public void guardar(ReservaSubasta reserva);
    public void actualizar(ReservaSubasta reserva);
}
