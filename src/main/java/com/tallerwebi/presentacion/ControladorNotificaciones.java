package com.tallerwebi.presentacion;

import ch.qos.logback.core.model.Model;
import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.ServicioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/cantidadNotificacionesNoLeidas")
    public @ResponseBody Object obtenerCantidadNotificacionesNoLeidasJSON(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("USUARIO");
        if(email == null) {
            return null;
        }

        List<Notificacion> notificacionesNoLeidas = servicioNotificacion.obtenerNoLeidas(email);
        Map<String, Object> responseReturn = new HashMap<>();
        Integer cantidadNotificacionesNoLeidas = notificacionesNoLeidas.size();

        responseReturn.put("cantidadNotificaciones",cantidadNotificacionesNoLeidas);

        return responseReturn;
    }

}