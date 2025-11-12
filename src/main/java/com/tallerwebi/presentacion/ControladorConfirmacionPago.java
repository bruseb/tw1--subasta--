package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioSubasta;
import com.tallerwebi.dominio.Subasta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorConfirmacionPago {

    private final ServicioSubasta servicioSubasta;

    @Autowired
    public ControladorConfirmacionPago(ServicioSubasta servicioSubasta) {
        this.servicioSubasta = servicioSubasta;
    }

    @GetMapping("/confirmacionPagoEnvio")
    public String mostrarConfirmacion(@RequestParam(name = "idSubasta", required = false) Long idSubasta,
                                      Model model) {

        Subasta subasta = null;

        // 1. **BÚSQUEDA DE SUBASTA**
        // Solo buscamos si el ID no es nulo
        if (idSubasta != null) {
            try {
                subasta = servicioSubasta.buscarSubasta(idSubasta);
            } catch (Exception e) {
                System.err.println("Error al buscar la subasta con ID " + idSubasta + ": " + e.getMessage());
                // Si hay un error en el servicio/repo, 'subasta' seguirá siendo null
            }
        }

        // 2. 🚨 MANEJO SEGURO DE OBJETO NULO (EVITA EL EL1007E)
        if (subasta == null) {
            // Creamos un objeto Subasta dummy para que la vista pueda acceder a .titulo y .id
            subasta = new Subasta();
            // 🚨 Asegúrate de que tu clase Subasta tiene un setter para el título
            subasta.setTitulo("Detalle no encontrado o acceso directo");
            // Si la vista accede al ID, también necesita un valor:
            // subasta.setId(0L);
        }

        // 3. **AÑADIR AL MODELO**
        model.addAttribute("subasta", subasta);
        // model.addAttribute("pago", pago);

        // 4. DEVOLVER LA VISTA
        return "confirmacionPagoEnvio";
    }
}
