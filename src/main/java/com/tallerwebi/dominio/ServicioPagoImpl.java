package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public class ServicioPagoImpl implements ServicioPago {

    @Override
    public Float calcularMontoConReserva(Subasta subasta) {

        Float precioInicial = subasta.getPrecioInicial();
        if (precioInicial == null) {
            throw new RuntimeException("La subasta no tiene precio inicial definido.");
        }
        // 10% del monto inicial
        Float reservaPrevia = precioInicial * 0.10f;

        return reservaPrevia;
    }

}
