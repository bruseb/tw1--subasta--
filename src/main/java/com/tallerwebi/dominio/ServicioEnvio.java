package com.tallerwebi.dominio;

public interface ServicioEnvio {

    Envio calcularEnvio(Envio actual);

    String calcularTamanio(Double largo, Double ancho, Double alto);

    String calcularZona(String pais, String provincia);

    Double calcularCosto(String tamanio);

    Integer calcularTiempoEntrega(String zona);

}
