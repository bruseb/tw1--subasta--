package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.ServicioCategorias;
import com.tallerwebi.dominio.ServicioSubcategorias;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ControladorCategorias {

    private final ServicioCategorias servicioCategorias;
    private final ServicioSubcategorias servicioSubcategorias;

    public ControladorCategorias(ServicioCategorias servicioCategorias, ServicioSubcategorias servicioSubcategorias) {
        this.servicioCategorias = servicioCategorias;
        this.servicioSubcategorias = servicioSubcategorias;
    }


    @RequestMapping(path = "/categorias", method = RequestMethod.GET)
    public String mostrarCategoriasExistentes(Model model) {
        model.addAttribute("categorias", servicioCategorias.listarCategorias());
        model.addAttribute("subcategorias", servicioSubcategorias.listarSubcategorias());
        return "categorias";
    }

    @RequestMapping(path = "/categorias/{nombreDeCategoriaEnUrl}", method = RequestMethod.GET)

    //Usamos PathVariable para capturar el valor que viene en la URL
    public String verCategoria(@PathVariable("nombreDeCategoriaEnUrl") String nombreDeCategoriaEnUrl, Model model) {

        Categoria categoria = servicioCategorias.buscarCategoriaPorNombreDeRuta(nombreDeCategoriaEnUrl);

        model.addAttribute("categoria", categoria);
        return "pagina-categoria-seleccionada";
    }



}
