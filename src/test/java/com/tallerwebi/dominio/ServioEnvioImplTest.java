package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ServioEnvioImplTest {

    private ServicioEnvioImpl servicioEnvio;


    @BeforeEach
    void setUp() {
        servicioEnvio = new ServicioEnvioImpl();
    }

        @Test
        void testCalcularEnvio_PampeanoPequenio(){

            //preparacion
            Envio envio = new Envio();
            envio.setLargo(10.0);
            envio.setAncho(10.0);
            envio.setAlto(10.0);// volumen =1000
            envio.setPeso(2.0);
            envio.setPais("Argentina");
            envio.setProvincia("Buenos Aires");

            //ejecucion
            Envio resultado = servicioEnvio.calcularEnvio(envio);



            //valicacion
            assertThat(resultado.getTamanio(),is(equalTo("Pequeño")));
            assertThat(resultado.getZonaDestino(),is(equalTo("Pampeana")));
            assertThat(resultado.getCosto(),closeTo(5000.0 + 2.0 * 100 * 1.0,00.1));
            assertThat(resultado.getDiasDeEntrega(),is(2));

        }

        @Test
        void CalcularEnvio_ProvinciaInvalidaArgentina_lanzaExcepcion(){

            //preparacion
            Envio envio = new Envio();
            envio.setLargo(10.0);
            envio.setAncho(10.0);
            envio.setAlto(10.0);// volumen =1000
            envio.setPeso(2.0);
            envio.setPais("Argentina");
            envio.setProvincia("Rio de Janeiro");//invalida

            //ejecucion
            Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> servicioEnvio.calcularEnvio(envio));

            //validacion
            assertThat(exception.getMessage(), containsString("Provincia inválida para Argentina"));

    }
    @Test
    void testCalcularEnvio_internacional_valoresValido(){

        //preparacion
        Envio envio = new Envio();
        envio.setLargo(20.0);
        envio.setAncho(20.0);
        envio.setAlto(20.0);// volumen =1000
        envio.setPeso(1.5);
        envio.setPais("Brasil");
        envio.setProvincia("Rio de Janerio");

        //ejecucion
        Envio resultado = servicioEnvio.calcularEnvio(envio);



        //valicacion
        assertThat(resultado.getTamanio(),is(equalTo("Pequeño")));
        assertThat(resultado.getZonaDestino(),is(equalTo("Internacional")));
        assertThat(resultado.getCosto(),closeTo(5000.0 + 1.5 * 100 * 2.0,00.1));
        assertThat(resultado.getDiasDeEntrega(),is(10));

    }


        @Test
        void calcularTamanio_extraGrande(){

            //preparacion
            Envio envio = new Envio();
            envio.setLargo(50.0);
            envio.setAncho(50.0);
            envio.setAlto(50.0);// volumen = 125000
            envio.setPeso(2.0);
            envio.setPais("Argentina");
            envio.setProvincia("Buenos Aires");

            //ejecucion
            String tamanio = servicioEnvio.calcularTamanio(envio.getLargo(), envio.getAncho(), envio.getAlto());

            //validacion
            assertThat(tamanio, is(equalTo("Extra grande")));



        }



}

