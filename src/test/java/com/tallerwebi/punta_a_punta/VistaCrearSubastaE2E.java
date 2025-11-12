package com.tallerwebi.punta_a_punta;

import java.net.MalformedURLException;
import java.net.URL;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaCrearSubasta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaCrearSubastaE2E {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaCrearSubasta vistaCrearSubasta;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {

        context = browser.newContext();
        Page page = context.newPage();
        vistaCrearSubasta = new VistaCrearSubasta(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaEstarEnPaginaCrearSubastas() throws MalformedURLException{
        URL actualURL = vistaCrearSubasta.obtenerURLActual();
        assertThat(actualURL.getPath(), equalToIgnoringCase("/spring/crearSubasta"));
    }

    @Test
    void deberiaDarErrorSiIntentaCrearSubastaSinImagenes(){
        vistaCrearSubasta.escribirNombreProducto("Producto 1");
        vistaCrearSubasta.escribirDescripcion("Descripcion");
        vistaCrearSubasta.escribirEstadoProducto("NUEVO");
        vistaCrearSubasta.escribirPeso("15");
        vistaCrearSubasta.escribirLargo("15");
        vistaCrearSubasta.escribirAlto("15");
        vistaCrearSubasta.escribirAncho("15");
        vistaCrearSubasta.escribirCategoria("1");
        vistaCrearSubasta.escribirSubcategoria("1");
        vistaCrearSubasta.escribirPrecioInicial("1000");
        vistaCrearSubasta.escribirDuracion("1");
        //vistaCrearSubasta.escribirImagenes("TESTIMAGEN");
        vistaCrearSubasta.darClickEnCrear();
        String textoError = vistaCrearSubasta.obtenerMensajeDeError();
        assertThat("Imagen no definida.", equalToIgnoringCase(textoError));
    }

    @Test
    void deberiaDarErrorSiIntentaCrearSubastaSinEstadoProducto(){
        vistaCrearSubasta.escribirNombreProducto("Producto 1");
        vistaCrearSubasta.escribirDescripcion("Descripcion");
        vistaCrearSubasta.escribirEstadoProducto("NUEVO");
        vistaCrearSubasta.escribirPeso("15");
        vistaCrearSubasta.escribirLargo("15");
        vistaCrearSubasta.escribirAlto("15");
        vistaCrearSubasta.escribirAncho("15");
        //vistaCrearSubasta.escribirCategoria("1");
        //vistaCrearSubasta.escribirSubcategoria("1");
        vistaCrearSubasta.escribirPrecioInicial("1000");
        vistaCrearSubasta.escribirDuracion("1");
        vistaCrearSubasta.escribirImagenes("TESTIMAGEN");
        vistaCrearSubasta.darClickEnCrear();
        String textoError = vistaCrearSubasta.obtenerMensajeDeError();
        assertThat("Categoria no definida.", equalToIgnoringCase(textoError));
    }

    @Test
    void deberiaDarErrorSiIntentaCrearSubastaConLenguajeOfensivo(){
        vistaCrearSubasta.escribirNombreProducto("Producto 1");
        vistaCrearSubasta.escribirDescripcion("Descripcion de mierda");
        vistaCrearSubasta.escribirEstadoProducto("NUEVO");
        vistaCrearSubasta.escribirPeso("15");
        vistaCrearSubasta.escribirLargo("15");
        vistaCrearSubasta.escribirAlto("15");
        vistaCrearSubasta.escribirAncho("15");
        vistaCrearSubasta.escribirCategoria("1");
        vistaCrearSubasta.escribirSubcategoria("1");
        vistaCrearSubasta.escribirPrecioInicial("1000");
        vistaCrearSubasta.escribirDuracion("1");
        vistaCrearSubasta.escribirImagenes("TESTIMAGEN");
        vistaCrearSubasta.darClickEnCrear();
        String textoError = vistaCrearSubasta.obtenerMensajeDeError();
        assertThat("El título o la descripción contienen lenguaje ofensivo.", equalToIgnoringCase(textoError));
    }

    @Test
    void deberiaDarErrorSiIntentaCrearSubastaConImporteNegativo(){
        vistaCrearSubasta.escribirNombreProducto("Producto 1");
        vistaCrearSubasta.escribirDescripcion("Descripcion de mierda");
        vistaCrearSubasta.escribirEstadoProducto("NUEVO");
        vistaCrearSubasta.escribirPeso("15");
        vistaCrearSubasta.escribirLargo("15");
        vistaCrearSubasta.escribirAlto("15");
        vistaCrearSubasta.escribirAncho("15");
        vistaCrearSubasta.escribirCategoria("1");
        vistaCrearSubasta.escribirSubcategoria("1");
        vistaCrearSubasta.escribirPrecioInicial("-1000");
        vistaCrearSubasta.escribirDuracion("1");
        vistaCrearSubasta.escribirImagenes("TESTIMAGEN");
        vistaCrearSubasta.darClickEnCrear();
        String textoError = vistaCrearSubasta.obtenerMensajeDeError();
        assertThat("El monto inicial no puede ser negativo.", equalToIgnoringCase(textoError));
    }

}
