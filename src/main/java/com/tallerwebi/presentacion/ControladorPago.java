package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioOfertaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/pago")
public class ControladorPago {

    private final ServicioOferta servicioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final RepositorioOfertaImpl repositorioOferta;
    private final ServicioPago servicioPago;


    @Autowired
    public ControladorPago(ServicioOferta servicioOferta,
                           RepositorioUsuario repositorioUsuario,
                           ServicioSubasta servicioSubasta,
                           RepositorioOfertaImpl repositorioOferta, ServicioPago servicioPago) {
        this.servicioOferta = servicioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.repositorioOferta = repositorioOferta;
        this.servicioPago = servicioPago;
    }




    @GetMapping("/formPago/{idSubasta}")
    public String mostrarFormPago(@PathVariable Long idSubasta,
                                  Model model,
                                  HttpServletRequest request) {

        // 1. Buscar usuario logueado
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
        if(usuarioLogueado != null){
            model.addAttribute("emailUsuario", usuarioLogueado.getEmail());
        }

        // 2. Obtener la subasta desde el servicio o repositorio
        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);

        // 3. Tomar el monto actual (si no existe, usar precio inicial)
        Float montoActual = (subasta.getPrecioActual() != null) ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();

        Float reserva = servicioPago.calcularMontoConReserva(subasta);
        model.addAttribute("reservaPrevia", reserva);


        // 4. Enviar los datos al HTML

        model.addAttribute("emailUsuario", usuarioLogueado);
        model.addAttribute("subasta", subasta);
        model.addAttribute("montoActual", montoActual);

        return "formPago";
    }

    @PostMapping("/FormPago")          // opcional: si enviás el form por POST
    public String procesarPago(/* @ModelAttribute ... */) {
        return "redirect:/spring/ok";  // ejemplo
    }

}
