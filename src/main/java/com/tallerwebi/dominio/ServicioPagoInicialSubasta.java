package com.tallerwebi.dominio;

import org.springframework.transaction.annotation.Transactional;


public interface ServicioPagoInicialSubasta {


        PagoInicialSubasta buscarPagoConfirmado(Usuario usuario, Subasta subasta);
        void registrarPagoInicial(Usuario usuario, Subasta subasta);
    }

