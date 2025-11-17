package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ServicioPagoInicialSubastaImpl implements ServicioPagoInicialSubasta {

    private final RepositorioPagoInicialSubasta repositorioPagoInicialSubasta;

    @Autowired
    public ServicioPagoInicialSubastaImpl(RepositorioPagoInicialSubasta repositorioPagoInicialSubasta) {
        this.repositorioPagoInicialSubasta = repositorioPagoInicialSubasta;
    }

    @Override
    @Transactional(readOnly = true)
    public PagoInicialSubasta buscarPagoConfirmado(Usuario usuario, Subasta subasta) {
        return repositorioPagoInicialSubasta.buscarPagoInicialConfirmado(usuario, subasta);
    }

    @Override
    @Transactional
    public boolean registrarPagoInicial(Usuario usuario, Subasta subasta) {
        PagoInicialSubasta existente = repositorioPagoInicialSubasta.buscarPagoInicialConfirmado(usuario, subasta);
        if (existente != null && Boolean.TRUE.equals(existente.getPagoConfirmado())) {
            return false; // ya existe pago confirmado
        }

        // Calcular 10% con dos decimales
        BigDecimal monto = BigDecimal.valueOf(subasta.getPrecioInicial())
                .multiply(BigDecimal.valueOf(0.10))
                .setScale(2, RoundingMode.HALF_UP);

        PagoInicialSubasta nuevoPago = new PagoInicialSubasta();
        nuevoPago.setUsuario(usuario);
        nuevoPago.setSubasta(subasta);
        nuevoPago.setMontoPagado(monto);
        nuevoPago.setPagoConfirmado(true);
        nuevoPago.setFechaPago(LocalDateTime.now());

        repositorioPagoInicialSubasta.guardar(nuevoPago);
        return true;
    }

    @Override
    @Transactional
    public boolean registrarPagoInicial(Usuario usuario, Subasta subasta, String numeroTarjeta) {
        PagoInicialSubasta existente = repositorioPagoInicialSubasta.buscarPagoInicialConfirmado(usuario, subasta);
        if (existente != null && Boolean.TRUE.equals(existente.getPagoConfirmado())) {
            return false; // ya existe pago confirmado
        }

        // Calcular 10% con dos decimales
        BigDecimal monto = BigDecimal.valueOf(subasta.getPrecioInicial())
                .multiply(BigDecimal.valueOf(0.10))
                .setScale(2, RoundingMode.HALF_UP);

        PagoInicialSubasta nuevoPago = new PagoInicialSubasta();
        nuevoPago.setUsuario(usuario);
        nuevoPago.setSubasta(subasta);
        nuevoPago.setMontoPagado(monto);
        nuevoPago.setPagoConfirmado(true);
        nuevoPago.setFechaPago(LocalDateTime.now());

        if(numeroTarjeta != null && numeroTarjeta.length() >= 4){
            String ultimos4 = numeroTarjeta.substring(numeroTarjeta.length() - 4);
            nuevoPago.setUltimosDigitosTarjeta(ultimos4);
        }

        repositorioPagoInicialSubasta.guardar(nuevoPago);
        return true;
    }
}