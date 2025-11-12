package com.tallerwebi.dominio;

public class Envio {

    private Double peso;
    private Double largo;
    private Double alto;
    private Double ancho;
    private String pais;
    private String provincia;
    private String ciudad;
    private Integer codigoPostal;
    private String tamanio;
    private Double costo;
    private Integer diasDeEntrega;
    private String zonaDestino;
    private String direccion;


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getLargo() { return largo; }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAlto() {
        return alto;
    }

    public void setAlto(Double alto) {
        this.alto = alto;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public Integer getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(Integer codigoPostal) { this.codigoPostal = codigoPostal; }

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


