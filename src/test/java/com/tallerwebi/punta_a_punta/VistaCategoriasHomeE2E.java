package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaCategoriasHome;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.net.URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import static org.hamcrest.Matchers.*;

public class VistaCategoriasHomeE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaCategoriasHome vistaCategoriasHome;

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
        vistaCategoriasHome = new VistaCategoriasHome(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaHaberAlMenosUnaCategoriasEnElNav() throws MalformedURLException {
        dadoQueElUsuarioEstaEnLaVistaDeCategorias();
        elUsuarioDeberiaVerAlMenosUnaCategoriaEnElNav();
    }

    @Test
    void estandoEnNavCategoriasElUsuarioDeberiaVerAlMenosUnaSubCategoria() throws MalformedURLException {
        dadoQueElUsuarioEstaEnLaVistaDeCategorias();
        elUsuarioDeberiaVerAlMenosUnaCategoriaEnElNav();
        entoncesDeberiaVerAlMenosUnaSubcategoriaEnElNav();
    }

    @Test
    void deberiaListarCategoriasYSubcategoriasConLinksValidos() throws MalformedURLException {
        dadoQueElUsuarioEstaEnLaVistaDeCategorias();
        elUsuarioDeberiaVerAlMenosUnaCategoriaEnElNav();
        entoncesDeberiaTenerUnLinkValidoDeCategoria();
        entoncesDeberiaVerAlMenosUnaSubcategoriaEnElNav();
        entoncesDeberiaTenerUnLinkValidoDeSubcategoria();
    }

    @Test
    void deberiaVerElTituloDeCategoriasPopulares() throws MalformedURLException {
        dadoQueElUsuarioEstaEnLaVistaDeCategorias();
        entoncesDeberiaVerElTituloDeCategoriasPopulares();
    }

    @Test
    void deberiaExistirAlMenosUnaCategoriaPopularYTenerUnLinkValido() throws MalformedURLException {
        elUsuarioDeberiaVerAlMenosUnaCategoriaPopular();
        entoncesDeberiaTenerUnLinkValidoDeCategoria();
    }


    @Test
    void categoriasPopularesDeberianRenderizarImagenes() {
        dadoQueElUsuarioSeleccionaUnoDeLosElementosDeCategoriasPopulares();
        entoncesDeberiaRenderizarImagenes();
    }

    private void dadoQueElUsuarioEstaEnLaVistaDeCategorias() throws MalformedURLException {
        URL urlCategorias = vistaCategoriasHome.obtenerURLActual();
        assertThat(urlCategorias.getPath(), matchesPattern("^/spring/categorias(?:;jsessionid=[^/\\s]+)?$"));
    }

    private int obtenerCantidadDeCategoriasEnNav() {
        return (int) vistaCategoriasHome.categoriasNavItems().count();
    }

    private void elUsuarioDeberiaVerAlMenosUnaCategoriaEnElNav()  {
        int cantidadCategorias = obtenerCantidadDeCategoriasEnNav();
        assertThat(cantidadCategorias, greaterThan(0));
    }

    private void entoncesDeberiaVerAlMenosUnaSubcategoriaEnElNav() {
        int cantidad = obtenerCantidadDeSubcategoriasEnNav();
        assertThat("Debería haber al menos una subcategoría visible en el nav",
                cantidad, greaterThan(0));
    }

    private int obtenerCantidadDeSubcategoriasEnNav() {
        return (int) vistaCategoriasHome.subcategoriasLinks().count();
    }

    private void entoncesDeberiaTenerUnLinkValidoDeSubcategoria() {
        String hrefSub = vistaCategoriasHome.subcategoriasLinks().first().getAttribute("href");
        assertThat(hrefSub, matchesPattern("^/spring/categorias/[^/]+/\\d+/[^/]+/\\d+$"));
    }

    private void entoncesDeberiaTenerUnLinkValidoDeCategoria() {
        String hrefCat = vistaCategoriasHome.linksCategorias().first().getAttribute("href");
        assertThat(hrefCat, matchesPattern("^/spring/categorias/[^/]+/\\d+$"));
    }

    private void elUsuarioDeberiaVerAlMenosUnaCategoriaPopular() {
        int cantidadPopulares = obtenerCantidadDeCategoriasPopulares();
        assertThat(cantidadPopulares, greaterThan(0));
    }

    private int obtenerCantidadDeCategoriasPopulares() {
        return (int) vistaCategoriasHome.popularesItems().count();
    }

    private void dadoQueElUsuarioSeleccionaUnoDeLosElementosDeCategoriasPopulares() {
        vistaCategoriasHome.popularesLinks().first().click();
    }

    private void entoncesDeberiaRenderizarImagenes() {
        var imgs = vistaCategoriasHome.imagenesEnListaDecategoriasPopulares();
        int count = (int) imgs.count();
        assertThat("Posee imagenes el bloque de categorías populares", count, greaterThanOrEqualTo(0));
    }

    private void entoncesDeberiaVerElTituloDeCategoriasPopulares() {
        vistaCategoriasHome.bloquePopulares().waitFor();
        String titulo = vistaCategoriasHome.popularesTitulo().textContent();
        assertThat(titulo, equalToIgnoringCase("Categorías Populares"));
    }
}

