package com.tallerwebi.dominio;


import com.tallerwebi.exception.UsuarioNoDefinidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("servicioOferta")
@Transactional
public class ServicioOfertaImpl  implements ServicioOferta{

    private final RepositorioOferta repositorioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSubasta repositorioSubasta;

    @Autowired
    public ServicioOfertaImpl(RepositorioOferta repositorioOferta, RepositorioUsuario repositorioUsuario, RepositorioSubasta repositorioSubasta) {
        this.repositorioOferta = repositorioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSubasta = repositorioSubasta;
    }



    @Override
    public Oferta ofertar(Long id, String emailCreador, Float montoOfertado) {
        // 1) Validar parámetros base
        if (emailCreador == null || emailCreador.isBlank())
            throw new UsuarioNoDefinidoException("Usuario no definido.");
        if (id == null)
            throw new IllegalArgumentException("idSubasta es obligatorio.");
        if (montoOfertado == null)
            throw new IllegalArgumentException("El monto ofertado es obligatorio.");

        // 2) Cargar usuario y subasta
        Usuario usuario = repositorioUsuario.buscar(emailCreador);
        if (usuario == null) throw new RuntimeException("Usuario inexistente.");

        Subasta subasta = repositorioSubasta.obtenerSubasta(id); // unificá el nombre en el repo
        if (subasta == null) throw new RuntimeException("Subasta inexistente.");

        // 3) Regla de negocio (Float)
        Float actual = subasta.getPrecioActual() != null ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();
        if (actual == null) throw new RuntimeException("La subasta no tiene precio inicial configurado.");
        if (Float.compare(montoOfertado, actual) <= 0)
            throw new RuntimeException("El monto ofertado debe ser mayor a " + actual);

        // 4) Construir y guardar la oferta
        Oferta oferta = new Oferta();
        oferta.setSubasta(subasta);
        oferta.setOfertadorID(usuario);
        oferta.setMontoOfertado(montoOfertado);
        oferta.setFechaOferta(LocalDateTime.now());
        repositorioOferta.guardarOferta(oferta);

        // 5) Actualizar estado de la subasta
        subasta.setPrecioActual(montoOfertado);
        repositorioSubasta.actualizar(subasta); // o confiar en @Transactional

        return oferta;

    }

    @Override
    public Object[] listarOfertasSubastaJSON(Long idSubasta){
        return repositorioOferta.obtenerOfertasPorSubastaJSON(idSubasta);
    }
}
