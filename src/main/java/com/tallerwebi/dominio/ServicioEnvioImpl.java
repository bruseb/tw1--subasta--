package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public class ServicioEnvioImpl implements ServicioEnvio {

    @Override
    public Envio calcularEnvio (Envio actual){
        String tamanio = calcularTamanio(actual.getLargo(),actual.getAncho(),actual.getAlto());
        String zona = calcularZona(actual.getPais(), actual.getProvincia());
        Double costo = calcularCosto(tamanio, actual.getPeso(), zona);
        Integer dias = calcularTiempoEntrega(zona);

        Envio respuesta = new Envio();
        respuesta.setTamanio(tamanio);
        respuesta.setZonaDestino(zona);
        respuesta.setCosto(costo);
        respuesta.setDiasDeEntrega(dias);

        return respuesta;
    }

    public String calcularTamanio(Double largo, Double ancho, Double alto){
        Double volumen = largo * ancho * alto;
        if(volumen <= 10000) return "Pequeño";
        else if (volumen <= 30000) return "Mediano";
        else if (volumen <= 60000) return "Grande";
        else return "Extra grande";
    }

    public String calcularZona (String pais, String provincia){
        if(!pais.equalsIgnoreCase("Argentina")) return "Internacional";

        switch(provincia.toLowerCase()){
            case "jujuy":
            case "salta":
            case "tucumán":
            case "catamarca":
            case "la rioja":
            case "santiago del estero":
                return "Noroeste";
            case "formosa":
            case "chaco":
            case "corrientes":
            case "misiones":
                return "Noreste";
            case "mendoza":
            case "san juan":
            case "san luis":
                return "Cuyo";
            case "buenos aires":
            case "cordoba":
            case "santa fe":
            case "entre rios":
            case "la pampa":
                return "Pampeana";
            case "neuquen":
            case "rio negro":
            case "chubut":
            case "santa cruz":
            case "tierra del fuego":
                return "Patagonia";
            default:
                return "Desconocida";
        }
    }

    public Double calcularCosto(String tamanio, Double peso, String zona){
        Double valorTamanio;

        switch(tamanio){
            case "Pequeño":
                valorTamanio = 5000.0;
                break;
            case "Mediano":
                valorTamanio = 7500.0;
                break;
            case "Grande":
                valorTamanio = 10000.0;
                break;
            default:
                valorTamanio = 13000.0;
                break;
        }

        Double valorZona;

        switch (zona){
            case "Noroeste":
            case "Noreste":
            valorZona = 1.2;
            break;
            case "Cuyo":
                valorZona = 1.3;
                break;
            case "Pampeana":
                valorZona = 1.0;
                break;
            case "Patagonia":
                valorZona = 1.5;
                break;
            case "Internacional":
                valorZona = 2.0;
                break;
            default:
                valorZona = 1.0;
        }

        return valorTamanio + peso * 100 * valorZona;
    }

    public Integer calcularTiempoEntrega (String zona){

        switch (zona){
            case "Pampeana":
                return 2;
            case "Cuyo":
                return 4;
            case "Noroeste":
                return 4;
            case "Noreste":
                return 4;
            case "Patagonia":
                return 6;
            case "Internacional":
            return 10;
            default:
                return 5;
        }
    }

}
