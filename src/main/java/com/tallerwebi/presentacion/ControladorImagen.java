package tu.paquete.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tu.paquete.servicio.ServicioImagen;

@Controller
public class ControladorImagen {

    private final ServicioImagen servicioImagen;

    public ControladorImagen(ServicioImagen servicioImagen) {
        this.servicioImagen = servicioImagen;
    }

    // Página con el formulario de subida
    @GetMapping("/imagen/subir")
    public ModelAndView irASubida() {
        return new ModelAndView("subir-imagen"); // Thymeleaf
    }

    // Maneja el POST del form multipart
    @PostMapping(path = "/imagen/subir", consumes = {"multipart/form-data"})
    public ModelAndView subir(@RequestParam("archivo") MultipartFile archivo) {
        String urlSegura = servicioImagen.subirImagen(archivo);

        ModelMap modelo = new ModelMap();
        modelo.put("urlImagen", urlSegura); // enviamos la URL a la vista de resultado

        return new ModelAndView("resultado-imagen", modelo);
    }
}