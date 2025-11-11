package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {
    void guardar(Notificacion notificacion);
    List<Notificacion> obtenerNoLeidas(String email);
    void marcarComoLeida(Long id);
}
