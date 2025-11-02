package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioOfertaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ofertar")

public class ControladorOfertar {


    private final ServicioOferta servicioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final RepositorioOfertaImpl repositorioOferta;


    @Autowired
    public ControladorOfertar(ServicioOferta servicioOferta,
                              RepositorioUsuario repositorioUsuario,
                              ServicioSubasta servicioSubasta, RepositorioOfertaImpl repositorioOferta) {
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
    public String mostrarFormularioOferta(@RequestParam Long idSubasta, Model model) {
        Subasta subastaDet = servicioSubasta.buscarSubasta(idSubasta);
        if (subastaDet == null) {
            model.addAttribute("error", "no existe la subasta" + idSubasta);
            return "error";
        }
        model.addAttribute("subastaDet", subastaDet);
        model.addAttribute("oferta", new Oferta());

        return "nuevaOferta"; // Thymeleaf buscará nuevaOferta.html
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
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("subastaDet", s);
            model.addAttribute("oferta", oferta);
            return "nuevaOferta";
        }
  /*
        // 2) Buscar subasta y usuario
        Subasta subasta = servicioSubasta.buscarSubasta(id);
        if (subasta == null) {
            model.addAttribute("error", "La subasta no existe (ID " + id + ").");
            return "error";
        }

        Usuario usuario = repositorioUsuario.buscar(creadorEmail);
        if (usuario == null) {
            model.addAttribute("error", "No se encontró el usuario logueado.");
            return "error";
        }

        // 3) Validar monto

        Float precioActual = subasta.getPrecioActual();
        if (precioActual == null){
            precioActual= subasta.getPrecioActual();
            subasta.setPrecioActual(precioActual);
        }

        Float ofertado = oferta.getMontoOfertado();
        if (ofertado == null) {
            model.addAttribute("error", "El monto ofertado es obligatorio.");
            model.addAttribute("subastaDet", subasta);
            model.addAttribute("oferta", new Oferta());
            return "nuevaOferta";
        }

        if (ofertado <= precioActual) {
            model.addAttribute("error", "El monto ofertado debe ser mayor a $" + precioActual);
            model.addAttribute("subastaDet", subasta);
            model.addAttribute("oferta", new Oferta());
            return "nuevaOferta";
        }

        // 4) Completar y guardar oferta
        oferta.setOfertadorID(usuario);
        oferta.setFechaOferta(LocalDateTime.now());
        oferta.setSubasta(subasta);
        servicioOferta.crearOferta(oferta, creadorEmail);

        // 5) Actualizar subasta
        subasta.setPrecioActual(ofertado);
        servicioSubasta.actualizar(subasta);

        // 6) Respuesta a la vista
        model.addAttribute("ultimaOferta", oferta);
        model.addAttribute("subastaDet", subasta);
        model.addAttribute("oferta", new Oferta());
        return "resultadoOferta";
    }*/
    }

    @RequestMapping(value="/ultimaOferta/{idSubasta}", method = RequestMethod.GET)
    public @ResponseBody Object obtenerUltimaOferta(@PathVariable Long idSubasta) {

        Subasta subastaDet = servicioSubasta.buscarSubasta(idSubasta);
        if (subastaDet == null) {
            return null;
        }

        Object[] listaOfertas = servicioOferta.listarOfertasSubasta(idSubasta);
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("listaOfertas", listaOfertas);

        return responseReturn;
    }
}









