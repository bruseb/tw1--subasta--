package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class VistaSubcategorias extends VistaWeb{

    public VistaSubcategorias(Page page) {
    super(page);
    page.navigate("localhost:8080/spring/subcategorias");
    }

    // Head
        public Locator tituloPagina() { return page.locator("head title"); }    

        // Subastas
        public Locator cantidadDeCardsRenderizados() { return page.locator(".card-subcategoria");}
        public Locator linksDeLasCardsDeSubcategorias() { return page.locator(".card-subcategoria a"); }
        public Locator descripcionDeLasCardsDeSubcategorias() { return page.locator(".card-subcategoria .descripcion"); }
        public Locator imagenesDeLasCardsDeSubcategorias() { return page.locator(".card-subcategoria img"); }      
        public Locator formatoDePrecio() { return page.locator(".card-subcategoria .precio");   }
        public Locator navegacionAlFormularioDeCrearSubasta() { return page.locator("text=Crear Subasta"); }

        public String pathActual() throws java.net.MalformedURLException {
            return this.obtenerURLActual().getPath();
        }
        public String pathDePruebaSubcategoriaElectrodomestico() throws java.net.MalformedURLException {
            return "/spring/categorias/electronica/1/electrodomesticos/6";
        }
    }
