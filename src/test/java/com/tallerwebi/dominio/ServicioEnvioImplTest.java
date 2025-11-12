package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioEnvioImplTest {

    private ServicioEnvioImpl servicioEnvio;

    @BeforeEach
    void setUp() {
        servicioEnvio = new ServicioEnvioImpl();
    }

    @Test
    void calcularTamanioDebeRetornarPequenoCuandoVolumenEsMenorA10000() {
        // Preparación
        Double largo = 10.0, ancho = 10.0, alto = 50.0; // volumen = 5000

        // Ejecución
        String resultado = servicioEnvio.calcularTamanio(largo, ancho, alto);

        // Validación
        assertThat(resultado, is(equalTo("Pequeño")));
    }

    @Test
    void calcularZonaDebeRetornarPampeanaCuandoProvinciaEsCordoba() {
        // Preparación
        String pais = "Argentina";
        String provincia = "Cordoba";

        // Ejecución
        String zona = servicioEnvio.calcularZona(pais, provincia);

        // Validación
        assertThat(zona, is(equalTo("Pampeana")));
    }

    @Test
    void calcularZonaDebeLanzarExcepcionCuandoPaisNoEsArgentina() {
        // Preparación
        String pais = "Chile";
        String provincia = "Santiago";

        // Ejecución + Validación
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> servicioEnvio.calcularZona(pais, provincia));

        assertThat(ex.getMessage(), is(equalTo("Solo se permiten envíos dentro de Argentina.")));
    }

    @Test
    void calcularCostoDebeRetornar7500CuandoTamanioEsMediano() {
        // Preparación
        String tamanio = "Mediano";

        // Ejecución
        Double costo = servicioEnvio.calcularCosto(tamanio);

        // Validación
        assertThat(costo, is(equalTo(7500.0)));
    }

    @Test
    void calcularTiempoEntregaDebeRetornar2DiasCuandoZonaEsPampeana() {
        // Preparación
        String zona = "Pampeana";

        // Ejecución
        Integer dias = servicioEnvio.calcularTiempoEntrega(zona);

        // Validación
        assertThat(dias, is(equalTo(2)));
    }

    @Test
    void calcularEnvioDebeRetornarEnvioCompletoCorrecto() {
        // Preparación
        Envio envio = new Envio();
        envio.setLargo(20.0);
        envio.setAncho(20.0);
        envio.setAlto(50.0); // volumen = 20000 → Mediano
        envio.setPais("Argentina");
        envio.setProvincia("Cordoba");

        // Ejecución
        Envio resultado = servicioEnvio.calcularEnvio(envio);

        // Validación
        assertThat(resultado.getTamanio(), is(equalTo("Mediano")));
        assertThat(resultado.getZonaDestino(), is(equalTo("Pampeana")));
        assertThat(resultado.getCosto(), is(equalTo(7500.0)));
        assertThat(resultado.getDiasDeEntrega(), is(equalTo(2)));
    }
}
