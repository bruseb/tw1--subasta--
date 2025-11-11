package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/notificaciones")
public class ControladorNotificaciones {

    @Autowired
    private ServicioNotificacion servicioNotificacion;

    private Map<String, SseEmitter> usuariosActivos = new ConcurrentHashMap<>();

    @GetMapping("/stream")
    public SseEmitter stream(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("USUARIO");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        usuariosActivos.put(email, emitter);
        emitter.onCompletion(() -> usuariosActivos.remove(email));
        emitter.onTimeout(() -> usuariosActivos.remove(email));
        return emitter;
    }

    public void enviarNotificacion(String email, String mensaje) {
        SseEmitter emitter = usuariosActivos.get(email);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("nueva-notificacion").data(mensaje));
            } catch (Exception e) {
                usuariosActivos.remove(email);
            }
        }
    }
}