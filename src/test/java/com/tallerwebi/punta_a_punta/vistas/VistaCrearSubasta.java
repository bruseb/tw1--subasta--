package com.tallerwebi.punta_a_punta.vistas;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class VistaCrearSubasta extends VistaWeb{
    public VistaCrearSubasta(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/crearSubasta");
    }

    public Page getPage() {
        return page;
    }

    // Header
    public Locator tituloPagina() { return page.locator("head title"); }

    //Formulario Crear Subasta
    public Locator nombreSubasta() { return page.locator("#titulo"); }
    public Locator descripcionSubasta() { return page.locator("#descripcion"); }
    public Locator productoSubasta() { return page.locator("#estadoProducto"); }
    public Locator pesoSubasta() { return page.locator("#peso"); }
    public Locator largoSubasta() { return page.locator("#largo"); }
    public Locator altoSubasta() { return page.locator("#alto"); }
    public Locator anchoSubasta() { return page.locator("#ancho"); }
    public Locator categoriaSubasta() { return page.locator("#selectDeCategoria"); }
    public Locator subcategoriaSubasta() { return page.locator("#selectDeSubcategoria"); }
    public Locator precioInicial() { return page.locator("#precioInicial"); }
    public Locator duracion() { return page.locator("#select_fechaFin"); }
    public Locator imagenes() { return page.locator("#imagenSubasta"); }

    public Locator botonCrear() {  return page.locator("#btn-crearSubasta"); }
    public Locator mensajeError() { return page.locator("p.alert.alert-danger"); }

    public void escribirNombreProducto(String nombreProducto){
        this.nombreSubasta().type(nombreProducto);
    }

    public void escribirDescripcion(String descripcion){
        this.descripcionSubasta().type(descripcion);
    }

    public void escribirEstadoProducto(String estadoProducto){
        this.productoSubasta().type(estadoProducto);
    }

    public void escribirPeso(String peso){
        this.pesoSubasta().type(peso);
    }

    public void escribirLargo(String largo){
        this.largoSubasta().type(largo);
    }

    public void escribirAlto(String alto){
        this.altoSubasta().type(alto);
    }

    public void escribirAncho(String ancho){
        this.anchoSubasta().type(ancho);
    }

    public void escribirCategoria(String categoria){
        this.categoriaSubasta().type(categoria);
    }

    public void escribirSubcategoria(String subcategoria){
        this.subcategoriaSubasta().type(subcategoria);
    }

    public void escribirPrecioInicial(String precioInicial){
        this.precioInicial().type(precioInicial);
    }

    public void escribirDuracion(String duracion){
        this.duracion().type(duracion);
    }

    public void escribirImagenes(String imagenes){
        this.imagenes().type(imagenes);
    }

    public void darClickEnCrear(){
        this.botonCrear().click();
    }

    public String obtenerMensajeDeError(){
        return this.mensajeError().textContent();
    }
}
