package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioNotificacion {
    void crearNotificacion(Usuario destino, String mensaje);
    List<Notificacion> obtenerNoLeidas(String email);
    int contarNoLeidas(String email);
    List<Notificacion> obtenerTodas(String email);
    void marcarTodasComoLeidas(String email);
}
