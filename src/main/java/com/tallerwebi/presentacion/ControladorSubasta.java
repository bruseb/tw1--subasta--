package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Controller
public class ControladorSubasta {

    private final ServicioSubcategorias servicioSubcategorias;
    private final ServicioCategorias servicioCategorias;
    private ServicioSubasta servicioSubasta;
    private RepositorioSubasta repositorioSubasta;

    @Autowired
    public  ControladorSubasta(ServicioSubasta servicioSubasta, ServicioSubcategorias servicioSubcategorias, ServicioCategorias servicioCategorias, RepositorioSubasta repositorioSubasta) {
        this.servicioSubasta = servicioSubasta;
        this.servicioSubcategorias = servicioSubcategorias;
        this.servicioCategorias = servicioCategorias;
        this.repositorioSubasta = repositorioSubasta;
    }

    @RequestMapping(path = "/nuevaSubasta", method = RequestMethod.GET)
    public ModelAndView irANuevaSubasta() {
        ModelMap model = new ModelMap();
        model.put("subasta", new Subasta());
        List<Categoria> cat = servicioCategorias.listarCategorias();
        model.put("listaCategorias", cat);
        return new ModelAndView("nuevaSubasta", model);
    }

    @RequestMapping(path = "/crearSubasta", method = RequestMethod.POST)
    public ModelAndView crearSubasta(@ModelAttribute("subasta") Subasta subasta,
                                     @RequestParam("imagenSubasta") MultipartFile[] imagenSubasta,
                                     @RequestParam("precioInicial") Float precioInicial,
                                     HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try{

            subasta.setPrecioInicial(precioInicial);

            String creadorEmail = (String) request.getSession().getAttribute("USUARIO");

            servicioSubasta.crearSubasta(subasta,imagenSubasta, creadorEmail );

            return new ModelAndView("redirect:/confirmacion-subasta");

        }catch(NumberFormatException e){
            model.put("error","El monto ingresado no es válido");
            model.put("listaCategorias", servicioCategorias.listarCategorias());
            return new ModelAndView("nuevaSubasta", model);

        }catch(Exception e){
            model.put("error", e.getMessage());
            model.put("listaCategorias", servicioCategorias.listarCategorias());
            return new ModelAndView("nuevaSubasta", model);
        }
    }

        @RequestMapping(value = "/imagen/{subastaID}/{orden}")
        public ResponseEntity<byte[]> getImagenSubasta(@PathVariable("subastaID") Long subastaID,@PathVariable("orden") int orden, HttpServletRequest request){
            try{
                Subasta s = servicioSubasta.buscarSubasta(subastaID);
                orden = orden - 1;
                if(s.getId() != null && s.getId().equals(subastaID)) {   //CHECK QUE EXISTE LA SUBASTA
                    List<Imagen> listaImagenes = s.getImagenes();
                    Imagen temp = listaImagenes.get(orden);
                    if(temp != null){
                        byte[] imagenBytes =  java.util.Base64.getDecoder().decode(temp.getImagen());
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_JPEG);
                        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND );
            }catch(Exception e){
                return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND );
            }
        }

    @RequestMapping(path = "/confirmacion-subasta", method = RequestMethod.GET)
    public String confirmacionDeSubastaRealizada(Model model) {
        return "confirmacion-subasta";
    }


    @GetMapping ("/subastas")
    public String listarSubastas(@RequestParam(value = "titulo", required = false) String titulo, Model model){
        List<Subasta> subastas;

        if(titulo != null && !titulo.trim().isEmpty()){
            subastas = repositorioSubasta.buscarSubasta(titulo);
        }else {
            subastas = repositorioSubasta.buscarTodas();
        }
        model.addAttribute("subastas",subastas);
        return "listado";
    }
}
