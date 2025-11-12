package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioNotificacionImpl implements ServicioNotificacion {

    @Autowired
    private RepositorioNotificacion repositorioNotificacion;

    @Override
    public void crearNotificacion(Usuario destino, String mensaje) {
        Notificacion n = new Notificacion();
        n.setUsuarioDestino(destino);
        n.setMensaje(mensaje);
        n.setFecha(LocalDateTime.now());
        repositorioNotificacion.guardar(n);
    }

    @Override
    public List<Notificacion> obtenerNoLeidas(String email) {
        return repositorioNotificacion.obtenerNoLeidas(email);
    }

    @Override
    public int contarNoLeidas(String email) {
        return repositorioNotificacion.obtenerNoLeidas(email).size();
    }

    @Override
    public List<Notificacion> obtenerTodas(String email) {
        return repositorioNotificacion.obtenerTodas(email);
    }

    @Override
    public void marcarTodasComoLeidas(String email) {
        repositorioNotificacion.marcarTodasComoLeidas(email);
    }

}