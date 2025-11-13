package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioSubasta;
import com.tallerwebi.dominio.Subasta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorConfirmacionPago {

    private final ServicioSubasta servicioSubasta;

    @Autowired
    public ControladorConfirmacionPago(ServicioSubasta servicioSubasta) {
        this.servicioSubasta = servicioSubasta;
    }

    @GetMapping("/confirmacionPagoEnvio/{idSubasta}")
    public String mostrarConfirmacion(@PathVariable(name = "idSubasta") Long idSubasta,
                                      Model model) {


        if (idSubasta == null || idSubasta <= 0) {
            return "redirect:/compras";
        }

        Subasta subasta = null;

        try {
            // 2. OBTENER DATOS PARA LA CONFIRMACIÓN
            subasta = servicioSubasta.buscarSubasta(idSubasta);
        } catch (Exception e) {
            System.err.println("Error al buscar subasta: " + e.getMessage());
        }


        if (subasta == null) {
            subasta = new Subasta();
            subasta.setTitulo("Detalle no encontrado o transacción genérica");
        }


        model.addAttribute("subasta", subasta);

        return "confirmacionPagoEnvio";
    }
}
