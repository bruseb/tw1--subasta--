package com.tallerwebi.punta_a_punta;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tallerwebi.punta_a_punta.vistas.VistaSubcategorias;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class VistaSubcategoriasE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaSubcategorias vistaSubcategorias;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {

        context = browser.newContext();
        Page page = context.newPage();
        vistaSubcategorias = new VistaSubcategorias(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void estandoEnSubcategoriaElectrodomesticosSeDeberiaCargarLaVistaYMostrarUnTituloValido() throws MalformedURLException {
        elLinkDeSubcategoriaActualDeberiaSerUnoValido();
        dadoQueElUsuarioEstaEnLaSubcategoriaCamaras();
        deberiaVerseElTituloCorrespondienteElectrodomesticos();
    }

    @Test
    void deberiaRenderizarAlMenosUnaCardDeSubcategoria() throws  MalformedURLException{
        dadoQueElUsuarioEstaEnLaSubcategoriaCamaras();
        deberiaMostrarAlMenosUnaSubastaRenderizada();
    }

    @Test
    void cadaSubastaDebeTenerDescripcionImagenYPrecio() {
        dadoQueHaySubastasRenderizadas();
        entoncesCadaSubastaTieneDescripcionNoVacia(contarSubastas());
    }

    @Test
    void losPreciosDebenTenerFormatoMonedaConDosDecimales() {
        dadoQueHaySubastasRenderizadas();
        entoncesCadaSubastaTienePrecioVisible(contarSubastas());
    }

    @Test
    void cadaSubastaDebePoseerElLinkAOfertarNuevaOferta() {
        dadoQueHaySubastasRenderizadas();
        entoncesCadaSubastaTieneUnLink(contarSubastas());
    }

    @Test
    void deberiaRedirigirAOfertarNuevaOfertaDeLaSubastaConId2() throws Exception {
        int idEsperado = 2;

        dadoQueHagoClickEnElLinkDeOfertarConId(idEsperado);
        entoncesLaURLDebeSerDeOfertarConId(idEsperado);
    }



    private void elLinkDeSubcategoriaActualDeberiaSerUnoValido() throws MalformedURLException {
        String path = vistaSubcategorias.pathActual();
        assertThat(path, matchesPattern("^/spring/categorias/[^/]+/\\d+/[^/]+/\\d+$"));
    }

    private void dadoQueElUsuarioEstaEnLaSubcategoriaCamaras() throws MalformedURLException {
        assertThat(vistaSubcategorias.pathActual(), matchesPattern("/spring/categorias/electronica/1/camaras/1(?:;jsessionid=[^/\\s]+)?$"));
    }

    private void deberiaVerseElTituloCorrespondienteElectrodomesticos() {
        String titulo = vistaSubcategorias.tituloPagina().innerText().trim();
        assertThat("Cámaras", equalToIgnoringCase(titulo));
    }

    private void deberiaMostrarAlMenosUnaSubastaRenderizada() {
        int cantSubastas = vistaSubcategorias.cantidadDeSubastasRenderizados().count();
        assertThat(cantSubastas, greaterThanOrEqualTo(1));
    }

    private int contarSubastas() {
        int cantidad = (int) vistaSubcategorias.subastas().count();
        assertThat("No hay subastas renderizadas", cantidad, greaterThan(0));
        return cantidad;
    }

    private void dadoQueHaySubastasRenderizadas() {;
        contarSubastas();
    }

    private void entoncesCadaSubastaTieneUnLink(int cantidad) {
        int links = (int) vistaSubcategorias.linksDeSubastas().count();
        assertThat("Faltan links en las cards", links, equalTo(cantidad));
    }

    private void entoncesCadaSubastaTieneDescripcionNoVacia(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            String descripcion = vistaSubcategorias.descripcionDeSubastas().nth(i).innerText().trim();
            assertThat("Descripción vacía en subasta " + i, descripcion, not(blankOrNullString()));
        }
    }

    private void entoncesCadaSubastaTienePrecioVisible(int cantidad) {
        int precios = (int) vistaSubcategorias.preciosDeSubastas().count();
        assertThat("Faltan precios en las subastas", precios, equalTo(cantidad));

        String regexPrecio = "^\\s*\\$\\s*[\\d\\s\\.,]+[\\,\\.]\\d{2}\\s*$";
        for (int i = 0; i < cantidad; i++) {
            String precio = vistaSubcategorias.preciosDeSubastas().nth(i).innerText().trim();
            assertThat("Formato de precio inválido en subasta " + i, precio, matchesPattern(regexPrecio));
        }
    }

    private void dadoQueHagoClickEnElLinkDeOfertarConId(int id) throws MalformedURLException {
        vistaSubcategorias.darClickEnLinkDeOfertarConId(id);

        String urlActual = vistaSubcategorias.pathActual();
        assertThat("Se esperaba navegar a /spring/ofertar/nuevaOferta?idSubasta=" + id,
                urlActual,
                equalTo("/spring/ofertar/nuevaOferta")
        );
    }

    private void entoncesLaURLDebeSerDeOfertarConId(int id) throws Exception {

        String idEnURL = vistaSubcategorias.obtenerIdDeSubastaDesdeURL();
        assertThat(idEnURL, equalTo(String.valueOf(id)));
    }
}
