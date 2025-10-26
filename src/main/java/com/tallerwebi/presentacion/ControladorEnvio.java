package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.EnvioPedido;
import com.tallerwebi.dominio.EnvioRespuesta;
import com.tallerwebi.dominio.ServicioEnvioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorEnvio {

    @Autowired
    private ServicioEnvioImpl servicioEnvio;

    @GetMapping("/calcular-envio")
    public String mostrarFormulario(Model model){
        model.addAttribute("envioPedido", new EnvioPedido());
        return "formulario-envio";
    }

    @PostMapping("/calcular-envio")
    public String procesarFormulario(@ModelAttribute EnvioPedido envioPedido, Model model){
        EnvioRespuesta resultado = servicioEnvio.calcularEnvio(envioPedido);
        model.addAttribute("resultado", resultado);
        return "formulario-envio";
    }
}
