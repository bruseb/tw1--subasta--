package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Envio;
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
        model.addAttribute("envio", new Envio());
        return "formulario-envio";
    }

    @PostMapping("/calcular-envio")
    public String procesarFormulario(@ModelAttribute Envio envio, Model model){

        try {
            Envio resultado = servicioEnvio.calcularEnvio(envio);
            model.addAttribute("envio", envio);
            model.addAttribute("resultado", resultado);
        } catch (IllegalArgumentException e){
            model.addAttribute("envio", envio);
            model.addAttribute("error", e.getMessage());
        }
        return "formulario-envio";
    }
}
