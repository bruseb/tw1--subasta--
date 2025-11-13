package com.tallerwebi.dominio;

public interface ServicioPago {
    Float calcularMontoConReserva( Subasta subasta);
    public void registrarTransaccion(Long idSubasta, String emailUsuario, Float montoTotal, Integer estado);
}
