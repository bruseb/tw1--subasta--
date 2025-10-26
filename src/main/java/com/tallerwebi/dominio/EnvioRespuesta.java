package com.tallerwebi.dominio;

public class EnvioRespuesta {
    private String tamanio;
    private Double costo;
    private Integer diasDeEntrega;
    private String zonaDestino;

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Integer getDiasDeEntrega() {
        return diasDeEntrega;
    }

    public void setDiasDeEntrega(Integer diasDeEntrega) {
        this.diasDeEntrega = diasDeEntrega;
    }

    public String getZonaDestino() {
        return zonaDestino;
    }

    public void setZonaDestino(String zonaDestino) {
        this.zonaDestino = zonaDestino;
    }
}


