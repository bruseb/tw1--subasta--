package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.ServicioCategorias;
import com.tallerwebi.dominio.ServicioSubasta;
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
     private ServicioSubasta servicioSubastasMock;
     private Model modelMock;

     //ServicioCategorias servicioCategorias, ServicioSubasta servicioSubastas, ServicioSubcategorias
     @BeforeEach
     public void init() {
         servicioCategoriasMock = mock(ServicioCategorias.class);
         servicioSubastasMock = mock(ServicioSubasta.class);
         servicioSubcategoriasMock = mock(ServicioSubcategoriasImpl.class);
         modelMock = mock(Model.class);
         controladorCategorias = new ControladorCategorias(servicioCategoriasMock,servicioSubastasMock,servicioSubcategoriasMock);
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
     }

     @Test
     public void verCategoriaPorNombreDeUrlValidoDevuelveVistaYAgregaCategoriaAlModelo() {
         // preparación
         Categoria categoria = crearCategoria(1L, "Electrónica", "electronica");
         when(servicioCategoriasMock.buscarCategoriaConSusSubcategoriasPorNombreDeRuta("electronica"))
                 .thenReturn(categoria);

         // ejecución
         String vista = controladorCategorias.verCategoria("electronica",1L, modelMock);

         // validación
         assertThat(vista, equalToIgnoringCase("pagina-categoria-seleccionada"));
         verify(servicioCategoriasMock, times(1))
                 .buscarCategoriaConSusSubcategoriasPorNombreDeRuta("electronica");
         verify(modelMock, times(1)).addAttribute("categoria", categoria);
     }

     private Categoria crearCategoria(Long id, String nombre, String nombreEnUrl) {
         Categoria c = new Categoria();
         c.setId(id);
         c.setNombre(nombre);
         c.setNombreEnUrl(nombreEnUrl);
         return c;
     }
}
