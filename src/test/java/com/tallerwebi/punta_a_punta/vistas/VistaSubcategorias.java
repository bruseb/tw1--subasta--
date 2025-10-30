package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class VistaSubcategorias extends VistaWeb{

    public VistaSubcategorias(Page page) {
    super(page);
    page.navigate("localhost:8080/spring/categorias/electronica/1/camaras/1");
    }

    public Page getPage() {
        return page;
    }

    // Head
        public Locator tituloPagina() { return page.locator("head title"); }    

        // Subastas
        public Locator cantidadDeSubastasRenderizados() { return page.locator(".card-subcategoria");}
        public Locator subastas() {return page.locator(".card.card-subcategoria");}
        public Locator linksDeSubastas() {return page.locator(".card.card-subcategoria a");        }
        public Locator descripcionDeSubastas() {return page.locator(".card.card-subcategoria p.descripcion-recortada");}
        public Locator preciosDeSubastas() {return page.locator(".card.card-subcategoria h5.card-title");}

        public String pathActual() throws java.net.MalformedURLException {
        return this.obtenerURLActual().getPath();
        }

        public void darClickEnLinkDeOfertarConId(int idSubasta) {
            String selector = String.format(".card.card-subcategoria a[href='/spring/ofertar/nuevaOferta?idSubasta=%d']", idSubasta);
            page.locator(selector).first().click();
        }

        //Este método toma la URL actual del navegador, busca el número de subasta (idSubasta), y te devuelve ese número como texto.
        public String obtenerIdDeSubastaDesdeURL() throws java.net.URISyntaxException {
            java.net.URI uri = new java.net.URI(page.url()); //Convierte ese texto en un objeto URI, que es una forma más cómoda de trabajar con las partes de una dirección
            String query = uri.getQuery(); // devuelve "idSubasta=2"

            if (query != null && query.startsWith("idSubasta=")) {
                return query.split("=")[1]; // devuelve "2"
            }
            return null;
        }


    }
