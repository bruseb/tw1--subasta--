package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/pago-inicial")
public class ControladorPagoInicial {

    private final ServicioUsuario servicioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final ServicioPagoInicialSubasta servicioPagoInicialSubasta;

    @Autowired
    public ControladorPagoInicial(ServicioUsuario servicioUsuario,
                                  ServicioSubasta servicioSubasta,
                                  ServicioPagoInicialSubasta servicioPagoInicialSubasta) {
        this.servicioUsuario = servicioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.servicioPagoInicialSubasta = servicioPagoInicialSubasta;
    }

    @PostMapping
    public String procesarPagoInicial(@RequestParam Long idSubasta,
                                      @RequestParam String numeroTarjeta,
                                      @RequestParam String fechaVencimiento,
                                      @RequestParam String cvv,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttrs) {

        String emailUsuario = (String) request.getSession().getAttribute("USUARIO");
        if (emailUsuario == null) {
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesión para realizar el pago.");
            return "redirect:/login";
        }

        Usuario usuario = servicioUsuario.buscarPorEmail(emailUsuario);
        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);

        if (usuario == null || subasta == null) {
            redirectAttrs.addFlashAttribute("error", "No se pudo procesar el pago.");
            return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
        }

        // Validación de tarjeta
        if (numeroTarjeta.isBlank() || fechaVencimiento.isBlank() || cvv.isBlank()) {
            redirectAttrs.addFlashAttribute("error", "Debe completar los datos de la tarjeta.");
            // vuelve a pagoInicial porque no completó el pago
            return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
        }


        boolean pagoRegistrado = servicioPagoInicialSubasta.registrarPagoInicial(usuario, subasta, numeroTarjeta);

        if (pagoRegistrado) {
            redirectAttrs.addFlashAttribute("mensaje", "Pago inicial registrado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "Ya abonaste el 10% de esta subasta.");
        }

        //después del pago (nuevo o ya registrado) vuelve a nuevaOferta
        return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
    }
}



