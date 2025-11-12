package com.tallerwebi.presentacion;

import ch.qos.logback.core.model.Model;
import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.ServicioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
/*@RequestMapping("/notificaciones")*/
public class ControladorNotificaciones {

    private ServicioNotificacion servicioNotificacion;

    @Autowired
    public ControladorNotificaciones(ServicioNotificacion servicioNotificacion) {
        this.servicioNotificacion = servicioNotificacion;
    }

    @GetMapping("/notificaciones")
    public String verNotificaciones(HttpServletRequest request, ModelMap model) {
        String email = (String) request.getSession().getAttribute("USUARIO");

        if (email == null) {
            return "redirect:/login";
        }

        List<Notificacion> notificaciones = servicioNotificacion.obtenerTodas(email);
        servicioNotificacion.marcarTodasComoLeidas(email);

        model.addAttribute("notificaciones", notificaciones);
        return "notificaciones";
    }

    // contador de notificaciones en tiempo real
    @GetMapping("/notificaciones/contador")
    @ResponseBody
    public int obtenerContador(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("USUARIO");
        if (email == null) {
            return 0;
        }
        return servicioNotificacion.contarNoLeidas(email);
    }

    @GetMapping("/notificaciones/cantidad")
    @ResponseBody
    public Map<String, Object> obtenerCantidadNoLeidas(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("USUARIO");

        Map<String, Object> response = new HashMap<>();
        if (email == null) {
            response.put("cantidad", 0);
            return response;
        }

        int cantidad = servicioNotificacion.obtenerNoLeidas(email).size();
        response.put("cantidad", cantidad);
        return response;
    }
}