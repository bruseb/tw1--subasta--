package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ServicioPagoImpl implements ServicioPago {

    private final RepositorioPago repositorioPago;

    // Asegurarse de inyectar el repositorio
    @Autowired
    public ServicioPagoImpl(RepositorioPago repositorioPago) {
        this.repositorioPago = repositorioPago;
    }


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

    @Override
    @Transactional
    public void registrarTransaccion(Long idSubasta, String email, Float montoTotal, Integer estado) {
        Pago pago = new Pago();
        pago.setIdSubasta(idSubasta);
        pago.setEmail(email);
        pago.setMontoAbonado(montoTotal);
        pago.setEstado(estado); // Se establece el estado a 2 (Pagado)

        repositorioPago.guardarPago(pago);
    }

}
