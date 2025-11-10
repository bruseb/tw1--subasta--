package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class ServicioPagoInicialSubastaImpl implements ServicioPagoInicialSubasta{

    private final RepositorioPagoInicialSubasta repositorioPagoInicialSubasta;

    @Autowired
    public ServicioPagoInicialSubastaImpl(RepositorioPagoInicialSubasta repositorioPagoInicialSubasta){
        this.repositorioPagoInicialSubasta = repositorioPagoInicialSubasta;
    }

    @Override
    public PagoInicialSubasta buscarPagoConfirmado(Usuario usuario, Subasta subasta) {
        return repositorioPagoInicialSubasta.buscarPagoInicialConfirmado(usuario, subasta);
    }

    @Override
    public void registrarPagoInicial(Usuario usuario, Subasta subasta) {
        BigDecimal monto = new BigDecimal(subasta.getPrecioInicial()).multiply(new BigDecimal("0.10"));

        PagoInicialSubasta nuevoPago = new PagoInicialSubasta();
        nuevoPago.setUsuario(usuario);
        nuevoPago.setSubasta(subasta);
        nuevoPago.setMontoPagado(monto);
        nuevoPago.setPagoConfirmado(true);
        nuevoPago.setFechaPago(LocalDateTime.now());

        repositorioPagoInicialSubasta.guardar(nuevoPago);
    }
}
