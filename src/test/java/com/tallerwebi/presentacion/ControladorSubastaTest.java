package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.exception.UsuarioNoDefinidoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class ControladorSubastaTest {
    private ControladorSubasta controladorSubasta;
    private ServicioSubasta servicioSubastaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Subasta subastaMock;
    private MultipartFile multipartFileMock;


    @BeforeEach
    public void init() {
        servicioSubastaMock = mock(ServicioSubasta.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        controladorSubasta = new ControladorSubasta(servicioSubastaMock);
        multipartFileMock = mock(MultipartFile.class);
    }

    @Test
    public void crearSubastaDebeLlevarAconfirmacionCreacionDeSubasta() throws IOException {
        // preparación
        //when(multipartFileMock.getBytes()).thenReturn(null);
        when(sessionMock.getAttribute("USUARIO")).thenReturn("aguspucci@unlam.com");
        when(requestMock.getSession()).thenReturn(sessionMock);

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(subastaMock,multipartFileMock, requestMock);

        // validacion
        assertThat(vista.getViewName(), equalToIgnoringCase("redirect:/confirmacion-subasta"));
    }

    @Test
    public void crearSubastaCuandoServicioLanzaExcepcionImagenNoDefinidaDevuelveVistaNuevaSubastaConError() throws IOException {
        // preparación
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("USUARIO")).thenReturn("aguspucci@unlam.com");
        when(multipartFileMock.isEmpty()).thenReturn(true);
        Subasta subasta = new Subasta();

        // Configuración para lanzar la excepción
        doThrow(new RuntimeException("Imagen no definida."))
                .when(servicioSubastaMock)
                .crearSubasta(any(Subasta.class), any(MultipartFile.class), anyString());

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(subasta, multipartFileMock, requestMock);

        // validación
        assertThat(vista.getViewName(), equalToIgnoringCase("nuevaSubasta"));
        assertThat(String.valueOf(vista.getModel().get("error")), equalToIgnoringCase("Imagen no definida."));
    }

    @Test
    void crearSubastaServicioCuandoLanzaUsuarioNoDefinidoMuestraError() throws Exception {
        // preparación
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("USUARIO")).thenReturn(null); // email nulo
        when(multipartFileMock.isEmpty()).thenReturn(false);

        doThrow(new UsuarioNoDefinidoException("Usuario no definido."))
                .when(servicioSubastaMock)
                .crearSubasta(any(Subasta.class), any(MultipartFile.class), any());

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(new Subasta(), multipartFileMock, requestMock);

        // validacion
        assertThat(vista.getViewName(), equalToIgnoringCase("nuevaSubasta"));
        assertThat(String.valueOf(vista.getModel().get("error")), equalToIgnoringCase("Usuario no definido."));
    }

/*    @Test
    public void errorUsuarioNoSeteado() throws IOException {
        // preparación
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("USUARIO")).thenReturn(null);
        when(multipartFileMock.isEmpty()).thenReturn(false);

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(new Subasta(),multipartFileMock, requestMock);

        // validacion
        assertThat(vista.getViewName(), equalToIgnoringCase("error"));
        assertThat(vista.getModel().get("error").toString(), equalToIgnoringCase("Usuario no definido."));
    }*/

/*    @Test
    public void errorImagenNoSeteado() throws IOException {
        // preparación
        //when(multipartFileMock.getBytes()).thenReturn(null);
        when(sessionMock.getAttribute("USUARIO")).thenReturn("aguspucci@unlam.com");
        when(requestMock.getSession()).thenReturn(sessionMock);

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(subastaMock,multipartFileMock, requestMock);

        // validacion
        assertThat(vista.getViewName(), equalToIgnoringCase("nuevaSubasta"));
        assertThat(vista.getModel().get("error").toString(), equalToIgnoringCase("Imagen no definida."));
    }*/

/*    @Test
    public void errorCategoriaNoSeteado() throws IOException {
        //when(multipartFileMock.getBytes()).thenReturn(null);
        when(sessionMock.getAttribute("USUARIO")).thenReturn("aguspucci@unlam.com");
        when(requestMock.getSession()).thenReturn(sessionMock);

        // ejecución
        ModelAndView vista = controladorSubasta.crearSubasta(subastaMock,multipartFileMock, requestMock);

        // validacion
        assertThat(vista.getViewName(), equalToIgnoringCase("nuevaSubasta"));
        assertThat(vista.getModel().get("error").toString(), equalToIgnoringCase("Categoria no definida."));
    }*/


}
