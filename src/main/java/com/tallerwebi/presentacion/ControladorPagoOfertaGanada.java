package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/pagos")
public class ControladorPagoOfertaGanada {

    private final ServicioSubasta servicioSubasta;

    @Autowired
    public ControladorPagoOfertaGanada(ServicioSubasta servicioSubasta) {
        this.servicioSubasta = servicioSubasta;
    }

    @GetMapping("/mercado-pago/{idSubasta}")
    public String crearPreferenciaYRedirigir(@PathVariable Long idSubasta)
            throws MPException, MPApiException {

        // 1) Credencial
        MercadoPagoConfig.setAccessToken("APP_USR-8295022654599735-110920-2522ac8e2c1e088734a8126aab541295-2978914670");

        // 2) Traer subasta
        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);
        if (subasta == null) {
            throw new IllegalArgumentException("Subasta no encontrada: " + idSubasta);
        }

        // Título / descripción / precio (sin ganador por ahora)
        String titulo = (subasta.getTitulo() != null && !subasta.getTitulo().isBlank())
                ? subasta.getTitulo()
                : ("Subasta #" + subasta.getId());
        String descripcion = (subasta.getDescripcion() != null) ? subasta.getDescripcion() : "";
        BigDecimal precioActual = BigDecimal.valueOf(
                subasta.getPrecioActual() != null ? subasta.getPrecioActual()
                        : subasta.getPrecioInicial()
        );

        // 3) Back URLs
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://tu-dominio.com/pagos/success?idSubasta=" + idSubasta)
                .pending("https://tu-dominio.com/pagos/pending?idSubasta=" + idSubasta)
                .failure("https://tu-dominio.com/pagos/failure?idSubasta=" + idSubasta)
                .build();

        // 4) Ítem
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .id(String.valueOf(subasta.getId()))
                .title(titulo)
                .description(descripcion)
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(precioActual)
                .build();

        // 5) Expira a las 24 hs
        OffsetDateTime desde = OffsetDateTime.now(ZoneId.of("America/Argentina/Salta"));
        OffsetDateTime hasta = desde.plusHours(24);

        PreferenceRequest prefReq = PreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .notificationUrl("https://tu-dominio.com/webhooks/mercadopago")
                .externalReference("subasta-" + subasta.getId())
                .expires(true)
                .expirationDateFrom(desde)
                .expirationDateTo(hasta)
                .build();

        // 6) Crear preferencia y redirigir
        PreferenceClient client = new PreferenceClient();
        Preference pref = client.create(prefReq);

        return "redirect:" + pref.getSandboxInitPoint(); // o getInitPoint() en prod
    }
}

