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
    private RepositorioNotificacion repositorio;

    @Override
    public void crearNotificacion(Usuario destino, String mensaje) {
        Notificacion n = new Notificacion();
        n.setUsuarioDestino(destino);
        n.setMensaje(mensaje);
        n.setFecha(LocalDateTime.now());
        repositorio.guardar(n);
    }

    @Override
    public List<Notificacion> obtenerNoLeidas(String email) {
        return repositorio.obtenerNoLeidas(email);
    }
}