package com.tallerwebi.presentacion;

import java.util.List;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.ServicioCategorias;
import com.tallerwebi.dominio.ServicioSubasta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ControladorCategorias {

    private final ServicioCategorias servicioCategorias;
    private final ServicioSubasta servicioSubastas;

    public ControladorCategorias(ServicioCategorias servicioCategorias, ServicioSubasta servicioSubastas) {
        this.servicioCategorias = servicioCategorias;
        this.servicioSubastas = servicioSubastas;
    }

    @RequestMapping(path = "/categorias", method = RequestMethod.GET)
    public String mostrarCategoriasExistentes(Model model) {
        model.addAttribute("categorias", servicioCategorias.listarCategoriaConSubCategorias());
        return "categorias";
    }

    @RequestMapping(path = "/categorias/{nombreDeCategoriaEnUrl}/{idCategoria}", method = RequestMethod.GET)
    //Usamos PathVariable para capturar el valor que viene en la URL
    public String verCategoria(@PathVariable("nombreDeCategoriaEnUrl") String nombreDeCategoriaEnUrl, @PathVariable("idCategoria") Long idCategoria, Model model) {
        List<Subasta> subastas = servicioSubastas.listarSubastasPorCategoriaId(idCategoria);
        Categoria categoria = servicioCategorias.buscarCategoriaConSusSubcategoriasPorNombreDeRuta(nombreDeCategoriaEnUrl);

        model.addAttribute("categoria", categoria );
        model.addAttribute("subastas", subastas);
        return "pagina-categoria-seleccionada";
    }

}
