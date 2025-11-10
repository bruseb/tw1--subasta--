package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ControladorEnvio {

    @Autowired
    private ServicioEnvio servicioEnvio;

    @Autowired
    private RepositorioSubasta repositorioSubasta;

    @GetMapping("/calcular-envio")
    public String mostrarFormulario(Model model){
        model.addAttribute("envio", new Envio());
        model.addAttribute("subastas", repositorioSubasta.buscarTodas());
        return "formulario-envio";
    }

    @PostMapping("/calcular-envio")
    public String procesarFormulario(@ModelAttribute Envio envio,
                                     @RequestParam(required = false) Long subastaId,
                                     @RequestParam(required = false) String calcular,
                                     Model model) {

        if (subastaId != null) {
            Optional<Subasta> subasta = Optional.ofNullable(repositorioSubasta.obtenerSubasta(subastaId));
            subasta.ifPresent(s -> {
                envio.setLargo(s.getLargo());
                envio.setAncho(s.getAncho());
                envio.setAlto(s.getAlto());
                envio.setPeso(s.getPeso());
            });
        }

        if ("true".equals(calcular)) {
            try {
                Envio resultado = servicioEnvio.calcularEnvio(envio);
                model.addAttribute("resultado", resultado);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model.addAttribute("envio", envio);
        model.addAttribute("subastas", repositorioSubasta.buscarTodas());
        model.addAttribute("subastaId", subastaId);
        return "formulario-envio";
}
}
