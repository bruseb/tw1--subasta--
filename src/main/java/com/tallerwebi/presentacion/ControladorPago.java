package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioOfertaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller

public class ControladorPago {

    private final ServicioOferta servicioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final RepositorioOfertaImpl repositorioOferta;
    private final ServicioPago servicioPago;
    private final ServicioPerfil servicioPerfil;
    private final ServicioPagoInicialSubasta servicioPagoInicialSubasta;


    @Autowired
    public ControladorPago(ServicioOferta servicioOferta,
                           RepositorioUsuario repositorioUsuario,
                           ServicioSubasta servicioSubasta,
                           RepositorioOfertaImpl repositorioOferta, ServicioPago servicioPago, ServicioPerfil servicioPerfil, ServicioPagoInicialSubasta servicioPagoInicialSubasta) {
        this.servicioOferta = servicioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.repositorioOferta = repositorioOferta;
        this.servicioPago = servicioPago;
        this.servicioPerfil = servicioPerfil;
        this.servicioPagoInicialSubasta = servicioPagoInicialSubasta;

    }


    @GetMapping("/formPago/{idSubasta}")
    public String mostrarFormPago(@PathVariable Long idSubasta,
                                  Model model,
                                  HttpServletRequest request) {

        String email = (String) request.getSession().getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }


        HttpSession session = request.getSession();

        Usuario usuario = servicioPerfil.obtenerPerfil(email);



        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);


        if (subasta == null) {
            return "redirect:/compras";
        }

        PagoInicialSubasta pagoIncial = servicioPagoInicialSubasta.buscarPagoConfirmado(usuario,subasta);
        if(pagoIncial != null){
            model.addAttribute("ultimosDigitosTarjeta", pagoIncial.getUltimosDigitosTarjeta());
        }


        Float montoActual = (subasta.getPrecioActual() != null) ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();

        Float reserva = servicioPago.calcularMontoConReserva(subasta);
        model.addAttribute("reservaPrevia", reserva);



        Float costoEnvio = 0.0f;
        Long idSubastaEnSesion = (Long) session.getAttribute("idSubastaEnvio");

        if (idSubastaEnSesion != null && idSubastaEnSesion.equals(idSubasta)) {


            Object costoSesionObject = session.getAttribute("costoEnvioCalculado");

            if (costoSesionObject != null) {


                if (costoSesionObject instanceof Double) {
                    Double costoDouble = (Double) costoSesionObject;
                    costoEnvio = costoDouble.floatValue(); // Conversión segura
                }

                else if (costoSesionObject instanceof Float) {
                    costoEnvio = (Float) costoSesionObject;
                }


                session.removeAttribute("costoEnvioCalculado");
                session.removeAttribute("idSubastaEnvio");
            }
        }


        Float costoTotal = montoActual + costoEnvio - reserva;




        model.addAttribute("usuario", usuario);
        model.addAttribute("subasta", subasta);
        model.addAttribute("montoActual", montoActual);


        model.addAttribute("costoEnvio", costoEnvio);
        model.addAttribute("costoTotal", costoTotal);

        return "formPago";
    }

    @PostMapping("/formPago")
    public String procesarPago(@RequestParam("emailUsuario") String email,
                               @RequestParam("idSubasta") Long idSubasta,
                               @RequestParam("montoTotalPagado") Float costoTotal,

                               HttpServletRequest request,
                               Model model) {


        String emailSesion = (String) request.getSession().getAttribute("email");
        if (emailSesion == null || !emailSesion.equals(email)) {
            return "redirect:/login";
        }

        if (idSubasta == null || idSubasta <= 0) {
            return "redirect:/compras";
        }



        try {


            servicioPago.registrarTransaccion(idSubasta, email, costoTotal, 2);



        } catch (Exception e) {
            System.err.println("Error al procesar y guardar el pago: " + e.getMessage());
            model.addAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pagoFallido";
        }

        return "redirect:/confirmacionPagoEnvio?idSubasta=" + idSubasta;
    }




}
