package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioPagoInicialSubastaImplTest {

    private RepositorioPagoInicialSubasta repositorioMock;
    private ServicioPagoInicialSubastaImpl servicio;

    private Usuario usuario;
    private Subasta subasta;

    @BeforeEach
    void setUp() {
        repositorioMock = Mockito.mock(RepositorioPagoInicialSubasta.class);
        servicio = new ServicioPagoInicialSubastaImpl(repositorioMock);

        usuario = new Usuario();
        usuario.setEmail("test@correo.com");

        subasta = new Subasta();
        subasta.setPrecioInicial(1000.00F);
    }

    @Test
    void registrarPagoInicialDebeGuardarNuevoPagoCuandoNoExistePrevio(){

        //preparacion
        when(repositorioMock.buscarPagoInicialConfirmado(usuario,subasta)).thenReturn(null);

        //ejecucion
        boolean resultado = servicio.registrarPagoInicial(usuario,subasta);

        //validacion
        assertThat(resultado, is(equalTo(true)));

        ArgumentCaptor<PagoInicialSubasta> captor = ArgumentCaptor.forClass(PagoInicialSubasta.class);
        verify(repositorioMock).guardar(captor.capture());

        PagoInicialSubasta pagoGuardado = captor.getValue();
        assertThat(pagoGuardado.getUsuario(), is(equalTo(usuario)));
        assertThat(pagoGuardado.getSubasta(), is(equalTo(subasta)));
        assertThat(pagoGuardado.getMontoPagado(), is(equalTo(BigDecimal.valueOf(100.00).setScale(2))));
        assertThat(pagoGuardado.getPagoConfirmado(), is(equalTo(true)));
        assertThat(pagoGuardado.getFechaPago().getClass(), is(equalTo(LocalDateTime.now().getClass())));
    }
}
