package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.ServicioCategorias;
import com.tallerwebi.dominio.ServicioSubcategoriasImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ControladorCategoriaTest {

    private ControladorCategorias controladorCategorias;
    private ServicioCategorias servicioCategoriasMock;
    private ServicioSubcategoriasImpl servicioSubcategoriasMock;
    private Model modelMock;

    @BeforeEach
    public void init() {
        servicioCategoriasMock = mock(ServicioCategorias.class);
        modelMock = mock(Model.class);
        controladorCategorias = new ControladorCategorias(servicioCategoriasMock);
    }

    @Test
    public void deberiaMostrarCategoriasEnLaVista() {
        // preparación
        List<Categoria> lista = List.of(
                crearCategoria(1L, "Electrónica", "electronica"),
                crearCategoria(2L, "Hogar", "hogar")
        );
        when(servicioCategoriasMock.listarCategoriaConSubCategorias()).thenReturn(lista);

        // ejecución
        String vista = controladorCategorias.mostrarCategoriasExistentes(modelMock);

        // validación
        assertThat(vista, equalToIgnoringCase("categorias"));
        verify(modelMock, times(1)).addAttribute("categorias", lista);
        verify(servicioCategoriasMock, times(1)).listarCategoriaConSubCategorias();
        verifyNoMoreInteractions(servicioCategoriasMock, modelMock);
    }

    @Test
    public void siNoHayCategorias_deberiaEntregarListaVacia() {
        // preparación
        when(servicioCategoriasMock.listarCategoriaConSubCategorias()).thenReturn(List.of());

        // ejecución
        String vista = controladorCategorias.mostrarCategoriasExistentes(modelMock);

        // validación
        assertThat(vista, equalToIgnoringCase("categorias"));
        verify(modelMock).addAttribute(eq("categorias"), eq(List.of()));
        verify(servicioCategoriasMock).listarCategoriaConSubCategorias();
        verifyNoMoreInteractions(servicioCategoriasMock, modelMock);
    }

    @Test
    public void verCategoriaPorNombreDeUrlValidoDevuelveVistaYAgregaCategoriaAlModelo() {
        // preparación
        Categoria categoria = crearCategoria(1L, "Electrónica", "electronica");
        when(servicioCategoriasMock.buscarCategoriaConSusSubcategoriasPorNombreDeRuta("electronica"))
                .thenReturn(categoria);

        // ejecución
        String vista = controladorCategorias.verCategoria("electronica", modelMock);

        // validación
        assertThat(vista, equalToIgnoringCase("pagina-categoria-seleccionada"));
        verify(servicioCategoriasMock, times(1))
                .buscarCategoriaConSusSubcategoriasPorNombreDeRuta("electronica");
        verify(modelMock, times(1)).addAttribute("categoria", categoria);
        verifyNoMoreInteractions(servicioCategoriasMock, modelMock);
    }

    private Categoria crearCategoria(Long id, String nombre, String nombreEnUrl) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre(nombre);
        c.setNombreEnUrl(nombreEnUrl);
        return c;
    }

}
