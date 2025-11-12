package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {
    void guardar(Notificacion notificacion);
    List<Notificacion> obtenerNoLeidas(String email);
    List<Notificacion> obtenerTodas(String email);
    void marcarTodasComoLeidas(String email);
}
