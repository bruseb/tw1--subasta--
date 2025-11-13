package com.tallerwebi.punta_a_punta.vistas;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VistaCrearSubasta extends VistaWeb{
    public VistaCrearSubasta(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/");
    }

    public Page getPage() {
        return page;
    }

    // Header
    public Locator tituloPagina() { return page.locator("head title"); }

    //Login y ir a Subastas
    public void loginYIrACrearSubasta(){
        page.locator("#email").type("test@unlam.edu.ar");
        page.locator("#password").type("test");
        page.locator("#btn-login").click();
        page.navigate("localhost:8080/spring/nuevaSubasta");
    }

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
    public Locator mensajeError() { return page.locator(".alert.alert-danger"); }

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
        this.categoriaSubasta().selectOption(categoria);
    }

    public void escribirSubcategoria(String subcategoria){
        this.subcategoriaSubasta().selectOption(subcategoria);
    }

    public void escribirPrecioInicial(String precioInicial){
        this.precioInicial().type(precioInicial);
    }

    public void escribirDuracion(String duracion){
        this.duracion().type(duracion);
    }

    public void escribirImagenes(){
        //this.imagenes().type(imagenes);
        Path imagen = Paths.get("C:/Users/Pucci/Desktop/Imagenes test/492703872_1221103426047559_8010241550426300492_n.jpg");
        this.imagenes().setInputFiles(imagen);
    }

    public void darClickEnCrear(){
        this.botonCrear().click();
    }

    public String obtenerMensajeDeError(){
        Locator mensajeError = page.locator(".alert.alert-danger");
        return mensajeError.textContent();
    }
}
