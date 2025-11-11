package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioOfertaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ofertar")

public class ControladorOfertar {


    private final ServicioOferta servicioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final RepositorioOfertaImpl repositorioOferta;
    private boolean esPropietario(Subasta subasta, String emailUsuario) {
        return emailUsuario != null
                && subasta != null
                && subasta.getCreador() != null
                && subasta.getCreador().getEmail() != null
                && subasta.getCreador().getEmail().equalsIgnoreCase(emailUsuario);
    }

    @Autowired
    public ControladorOfertar(ServicioOferta servicioOferta,
                              RepositorioUsuario repositorioUsuario,
                              ServicioSubasta servicioSubasta,
                              RepositorioOfertaImpl repositorioOferta) {
        this.servicioOferta = servicioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.repositorioOferta = repositorioOferta;
    }



    // Vista inicial con el form para buscar
    @GetMapping("/buscarSubasta")
    public String mostrarFormularioBusqueda() {
        return "buscarSubasta";
    }

    // Procesar búsqueda
    @PostMapping("/buscarSubasta")
    public String buscarSubasta(@RequestParam Long idSubasta) {


        return "redirect:/ofertar/nuevaOferta?idSubasta=" + idSubasta;
    }


    // Mostrar la página de ofertar
    @GetMapping("/nuevaOferta")
    public String mostrarFormularioOferta(@RequestParam Long idSubasta,
                                          HttpServletRequest request,
                                          Model model) {
        Subasta subastaDet = servicioSubasta.buscarSubasta(idSubasta);

        if (subastaDet == null) {
            model.addAttribute("error", "no existe la subasta" + idSubasta);
            return "error";
        }

        // Obtener usuario logueado
        String emailUsuario = (String) request.getSession().getAttribute("USUARIO");
        boolean esPropietario = esPropietario(subastaDet, emailUsuario);


        model.addAttribute("subastaDet", subastaDet);
        model.addAttribute("oferta", new Oferta());
        model.addAttribute("esPropietario", esPropietario);

        return "nuevaOferta"; // Thymeleaf busca nuevaOferta.html
    }

    //Ver la subasta
    @GetMapping("/{id}")
    public String verDetalleSubasta(@PathVariable ("id") Long idSubasta, Model model) {
        Subasta subastaDet = servicioSubasta.buscarSubasta(idSubasta);
        if (subastaDet == null) {
            model.addAttribute("error", "no existe la subasta" + idSubasta);
            return "error";
        }
        model.addAttribute("subastaDet", subastaDet);
        return "subasta-detalle";
    }

    // Guardar la oferta
    @PostMapping("/guardar")

    public String guardarOferta(@ModelAttribute ("oferta") Oferta oferta,
                                @RequestParam Long idSubasta,
                                HttpServletRequest request,
                                Model model) {

        // Usuario logueado
        String creadorEmail = (String) request.getSession().getAttribute("USUARIO");
        if (creadorEmail == null) {
            model.addAttribute("error", "Debe iniciar sesión para ofertar.");
            return "redirect:/login";
        }

        try {
            Oferta creada = servicioOferta.ofertar(idSubasta, creadorEmail, oferta.getMontoOfertado());
            model.addAttribute("ultimaOferta", creada);
            model.addAttribute("subastaDet", creada.getSubasta());
            return "resultadoOferta";
        } catch (RuntimeException ex) {
            Subasta s = servicioSubasta.buscarSubasta(idSubasta);
            boolean esPropietario = esPropietario(s, creadorEmail);

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("subastaDet", s);
            model.addAttribute("oferta", oferta);
            model.addAttribute("esPropietario", esPropietario);
            return "nuevaOferta";
        }


    }

    @RequestMapping(value="/jsonOfertas/{idSubasta}", method = RequestMethod.GET)
    public @ResponseBody Object obtenerOfertasJSON(@PathVariable Long idSubasta) {

        Subasta subastaDet = servicioSubasta.buscarSubasta(idSubasta);
        if (subastaDet == null) {
            return null;
        }
        Object[] listaOfertas = servicioOferta.listarOfertasSubastaJSON(idSubasta);
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("listaOfertas", listaOfertas);

        return responseReturn;
    }
}









