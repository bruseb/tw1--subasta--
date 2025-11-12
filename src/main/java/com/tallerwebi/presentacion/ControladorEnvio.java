package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ControladorEnvio {

    @Autowired
    private ServicioEnvio servicioEnvio;
    @Autowired
    private ServicioPerfil servicioPerfil;

    @Autowired
    private RepositorioSubasta repositorioSubasta;

      //  @GetMapping("/calcular-envio")
       // public String mostrarFormulario(Model model){
         //   model.addAttribute("envio", new Envio());
          //  model.addAttribute("subastas", repositorioSubasta.buscarTodas());
           // return "formulario-envio";
        //}


    @GetMapping("/calcular-envio/{idSubasta}")
    public String mostrarFormularioEnvio(@PathVariable Long idSubasta,
                                         Model model,
                                         HttpServletRequest request) { // Necesitas esto para el email en sesión

        // 1. SEGURIDAD: Obtener el email del usuario logueado
        String email = (String) request.getSession().getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        // 2. OBTENER DATOS DEL USUARIO
        // 🚨 ASUME que tienes servicioPerfil inyectado y accesible
        Usuario usuario = servicioPerfil.obtenerPerfil(email);

        // 3. OBTENER DATOS DE LA SUBASTA (usando el método existente en tu repositorio)
        Subasta subasta = repositorioSubasta.obtenerSubasta(idSubasta);

        // 🛑 VALIDACIÓN (Manejo del NullPointer, como vimos antes)
        if (subasta == null) {
            return "redirect:/misSubastasGanadas"; // Redirigir si no existe
        }

        // 4. CREAR Y PRECARGAR EL OBJETO ENVIO
        Envio envio = new Envio();

        // a. Precargar datos de la SUBASTA (Tamaño)
        envio.setPeso(subasta.getPeso());
        envio.setLargo(subasta.getLargo());
        envio.setAncho(subasta.getAncho());
        envio.setAlto(subasta.getAlto());

        // b. Precargar datos del USUARIO (Domicilio)
        // 🚨 ASUME que Usuario tiene los getters para País, Provincia, etc.
        envio.setPais(usuario.getPais());
        envio.setProvincia(usuario.getProvincia());
        envio.setCiudad(usuario.getCiudad());
        envio.setDireccion(usuario.getDireccion());
        envio.setCodigoPostal(usuario.getCp());

        // c. Necesitas este ID para la lógica de POST, aunque esté precargado
        model.addAttribute("subastaId", idSubasta);

        // 5. AÑADIR AL MODELO
        model.addAttribute("envio", envio); // El objeto Envio precargado
        model.addAttribute("subastaGanada", subasta); // La subasta para mostrar info

        // 🚨 NOTA: Ya no se pasa 'subastas' (la lista de todas)
        //         porque no hay un <select> para elegir otra subasta.

        return "formulario-envio";
    }

    @PostMapping("/calcular-envio")
    public String procesarFormulario(@ModelAttribute Envio envio,
                                     @RequestParam(required = false) Long subastaId,
                                     @RequestParam(required = false) String calcular,
                                     Model model,
                                     HttpSession session) { // 🚨 1. INYECTAR LA SESIÓN

        Subasta subasta = repositorioSubasta.obtenerSubasta(subastaId);


        if ("true".equals(calcular) && subastaId != null) {

            try {
                // Aseguramos que el objeto 'envio' tenga las dimensiones correctas de la subasta
                if (subasta != null) {
                    envio.setLargo(subasta.getLargo());
                    envio.setAncho(subasta.getAncho());
                    envio.setAlto(subasta.getAlto());
                    envio.setPeso(subasta.getPeso());
                }

                Envio resultado = servicioEnvio.calcularEnvio(envio);
                model.addAttribute("resultado", resultado);

                // 🚨 2. GUARDAR EL COSTO Y EL ID EN LA SESIÓN
                if (resultado.getCosto() != null) {
                    session.setAttribute("costoEnvioCalculado", resultado.getCosto());
                    session.setAttribute("idSubastaEnvio", subastaId);
                }

            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model.addAttribute("envio", envio);
        model.addAttribute("subastaId", subastaId);

        model.addAttribute("subastaGanada", subasta);


        return "formulario-envio";
    }
}
