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

    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final ServicioPagoInicialSubasta servicioPagoInicialSubasta;

    @Autowired
    public ControladorPagoInicial(RepositorioUsuario repositorioUsuario,
                                  ServicioSubasta servicioSubasta,
                                  ServicioPagoInicialSubasta servicioPagoInicialSubasta) {
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.servicioPagoInicialSubasta = servicioPagoInicialSubasta;
    }

    @PostMapping
    public String procesarPagoInicial(@RequestParam Long idSubasta,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttrs) {

        String emailUsuario = (String) request.getSession().getAttribute("USUARIO");
        if (emailUsuario == null) {
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesión para realizar el pago.");
            return "redirect:/login";
        }

        Usuario usuario = repositorioUsuario.buscar(emailUsuario);
        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);

        if (usuario == null || subasta == null) {
            redirectAttrs.addFlashAttribute("error", "No se pudo procesar el pago.");
            return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
        }

        // Verificar si ya pagó
        PagoInicialSubasta pagoExistente = servicioPagoInicialSubasta.buscarPagoConfirmado(usuario, subasta);
        if (pagoExistente != null && Boolean.TRUE.equals(pagoExistente.getPagoConfirmado())) {
            redirectAttrs.addFlashAttribute("mensaje", "Ya abonaste el 10% de esta subasta.");
            return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
        }

        //registrar pago
        servicioPagoInicialSubasta.registrarPagoInicial(usuario, subasta);

        redirectAttrs.addFlashAttribute("mensaje", "Pago inicial registrado correctamente.");
        return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
    }
}


