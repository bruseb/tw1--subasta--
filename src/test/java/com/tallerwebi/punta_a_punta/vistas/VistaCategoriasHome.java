package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class VistaCategoriasHome extends VistaWeb{

    public VistaCategoriasHome(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/categorias");
    }

    // Navbar
    public Locator categoriasNavItems() { return page.locator("section.container-categorias .navbar-nav > li.nav-item"); }
    public Locator linksCategorias() { return page.locator("section.container-categorias .navbar-nav a.nav-link"); }
    public Locator subcategoriasLinks() { return page.locator(".badge-info li a.text-decoration-none"); }

    // Categorias Populares
    public Locator bloquePopulares() { return page.locator("#categorias-populares"); }
    public Locator popularesTitulo() { return page.locator("#categorias-populares h2"); }
    public Locator popularesItems() { return page.locator("#categorias-populares ul > li.col"); }
    public Locator imagenesEnListaDecategoriasPopulares() { return page.locator("#categorias-populares img"); }
    public Locator popularesLinks() { return page.locator("#categorias-populares a.d-block"); }


    public String pathActual() throws java.net.MalformedURLException {
        return this.obtenerURLActual().getPath();
    }

}
