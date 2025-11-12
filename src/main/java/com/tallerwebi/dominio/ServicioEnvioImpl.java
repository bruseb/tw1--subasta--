package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServicioEnvioImpl implements ServicioEnvio {

    @Override
    public Envio calcularEnvio(Envio actual) {
        String tamanio = calcularTamanio(actual.getLargo(), actual.getAncho(), actual.getAlto());
        String zona = calcularZona(actual.getPais(), actual.getProvincia());

        if (zona.equals("Internacional")) {
            throw new IllegalArgumentException("Solo se permiten envíos dentro de Argentina.");
        }

        Double costo = calcularCosto(tamanio);
        Integer dias = calcularTiempoEntrega(zona);

        Envio respuesta = new Envio();
        respuesta.setTamanio(tamanio);
        respuesta.setZonaDestino(zona);
        respuesta.setCosto(costo);
        respuesta.setDiasDeEntrega(dias);

        return respuesta;
    }

    public String calcularTamanio(Double largo, Double ancho, Double alto) {
        Double volumen = largo * ancho * alto;
        if (volumen <= 10000) return "Pequeño";
        else if (volumen <= 30000) return "Mediano";
        else if (volumen <= 60000) return "Grande";
        else if (volumen <= 100000) return "Extra Grande";
        else return "Gigante";
    }

    public String calcularZona(String pais, String provincia) {
        if (!pais.equalsIgnoreCase("Argentina")) {
            throw new IllegalArgumentException("Solo se permiten envíos dentro de Argentina.");
        }

        List<String> provinciasArgentina = Arrays.asList(
                "jujuy", "salta", "tucumán", "catamarca", "la rioja", "santiago del estero",
                "formosa", "chaco", "corrientes", "misiones", "mendoza", "san juan", "san luis",
                "buenos aires", "cordoba", "santa fe", "entre rios", "la pampa",
                "neuquen", "rio negro", "chubut", "santa cruz", "tierra del fuego");

        String provinciaLower = provincia.toLowerCase();
        if (!provinciasArgentina.contains(provinciaLower)) {
            throw new IllegalArgumentException("Provincia inválida para Argentina: " + provincia);
        }

        switch (provinciaLower) {
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

    public Double calcularCosto(String tamanio) {

        String tamanioNormalizado = tamanio.toLowerCase();
        switch (tamanioNormalizado) {
            case "pequeño":
                return 5000.0;
            case "mediano":
                return 7500.0;
            case "grande":
                return 10000.0;
            case "extra grande":
                return 12500.0;
            case "gigante":
                return 15000.0;
            default:
                throw new IllegalArgumentException("Tamaño desconocido " + tamanio);
        }
    }

        public Integer calcularTiempoEntrega (String zona){

            switch (zona) {
                case "Pampeana":
                    return 2;
                case "Cuyo":
                case "Noroeste":
                case "Noreste":
                    return 4;
                case "Patagonia":
                    return 6;
                default:
                    return 8;
            }
        }

    }
