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
import com.tallerwebi.punta_a_punta.vistas.VistaCategoriasHome;

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
    void deberiaCargarLaVistaDeSubcategoriasYMostrarUnTituloValido() throws MalformedURLException {
        assertThat(vistaSubcategorias.pathActual(), matchesPattern("^/spring/categorias/[^/]+/\\d+/[^/]+/\\d+$"));

        // El <title> existe y contiene “Subcategor”
        String titulo = vistaSubcategorias.tituloPagina().innerText();
        assertThat(titulo, not(blankOrNullString()));
        assertThat(titulo.toLowerCase(), containsString("subcategoria"));
    }

    @Test
    void deberiaRenderizarAlMenosUnaCardDeSubcategoria() {
        int cant = vistaSubcategorias.cantidadDeCardsRenderizados().count();
        assertThat("Se esperaba al menos una card .card-subcategoria", cant, greaterThanOrEqualTo(1));
    }

    @Test
    void cadaCardDebeTenerLinkDescripcionImagenYPrecio() {
        int cantCards = vistaSubcategorias.cantidadDeCardsRenderizados().count();

        // Links
        assertThat(vistaSubcategorias.linksDeLasCardsDeSubcategorias().count(), equalTo(cantCards));
        // Descripciones (no vacías)
        for (int i = 0; i < cantCards; i++) {
            String descripcion = vistaSubcategorias.descripcionDeLasCardsDeSubcategorias().nth(i).innerText().trim();
            assertThat("Descripción vacía en card " + i, descripcion, not(blankOrNullString()));
        }
        // Imágenes
        assertThat(vistaSubcategorias.imagenesDeLasCardsDeSubcategorias().count(), equalTo(cantCards));
        // (Opcional) verificar que cargaron “algo” (naturalWidth > 0)
        for (int i = 0; i < cantCards; i++) {
            Object naturalWidth = vistaSubcategorias.imagenesDeLasCardsDeSubcategorias().nth(i)
                .evaluate("img => img.naturalWidth");
            assertThat("La imagen no parece cargada en card " + i, ((Number) naturalWidth).intValue(), greaterThan(0));
        }
        // Precio presente
        assertThat(vistaSubcategorias.formatoDePrecio().count(), equalTo(cantCards));
    }

    @Test
    void losPreciosDebenTenerFormatoMonedaConDosDecimales() {
        // Acepta "$ 1.234,56" o "$ 1234.56" o "$ 1 234,56" (distintos separadores)
        String regexPrecio = "^\\s*\\$\\s*[\\d\\s\\.\\,]+[\\,\\.]\\d{2}\\s*$";
        int n = vistaSubcategorias.formatoDePrecio().count();
        for (int i = 0; i < n; i++) {
            String precio = vistaSubcategorias.formatoDePrecio().nth(i).innerText().trim();
            assertThat("Precio con formato inválido: " + precio, precio, matchesPattern(regexPrecio));
        }
    }

    @Test
    void alHacerClickEnUnaCardDebeNavegarADetalleDeSubcategoria() throws MalformedURLException {
        int cantLinks = vistaSubcategorias.linksDeLasCardsDeSubcategorias().count();
        assertThat(cantLinks, greaterThanOrEqualTo(1));

        // Click en el primero
        vistaSubcategorias.linksDeLasCardsDeSubcategorias().first().click();

        // La URL resultante debería ser de detalle/listado asociado a la subcategoría
        // Usamos un patrón flexible con id numérico.
        String path = vistaSubcategorias.pathActual();
        assertThat(
            "Se esperaba navegar a una ruta de detalle/listado de subcategoría",
            path,
            anyOf(
                matchesPattern("^/spring/subcategorias/\\d+(?:/.*)?(?:;jsessionid=[^/\\s]+)?$"),
                matchesPattern("^/spring/categorias/.+(?:;jsessionid=[^/\\s]+)?$") // por si tus links usan la ruta por categoría
            )
        );
    }

    @Test
    void elLinkCrearSubastaDebeNavegarAlFormulario() throws MalformedURLException {
        // Asegurar que el link exista y sea clickeable
        vistaSubcategorias.navegacionAlFormularioDeCrearSubasta().click();

        String path = vistaSubcategorias.pathActual();
        // Aceptamos variantes comunes: /subastas/nueva, /subastas/crear, /subasta/nueva
        assertThat(
            "Se esperaba navegar al formulario de creación de subasta",
            path,
            anyOf(
                containsString("/spring/subastas/nueva"),
                containsString("/spring/subastas/crear"),
                containsString("/spring/subasta/nueva"),
                containsString("/spring/subasta/crear")
            )
        );
    }

}
