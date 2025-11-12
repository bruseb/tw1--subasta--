package com.tallerwebi.dominio;

import com.tallerwebi.exception.UsuarioNoDefinidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("servicioOferta")
@Transactional
public class ServicioOfertaImpl implements ServicioOferta {

    private final RepositorioOferta repositorioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSubasta repositorioSubasta;
    private final ServicioPagoInicialSubasta servicioPagoInicialSubasta;
    private final ServicioNotificacion servicioNotificacion;

    @Autowired
    public ServicioOfertaImpl(RepositorioOferta repositorioOferta,
                              RepositorioUsuario repositorioUsuario,
                              RepositorioSubasta repositorioSubasta,
                              ServicioPagoInicialSubasta servicioPagoInicialSubasta) {
    public ServicioOfertaImpl(RepositorioOferta repositorioOferta, RepositorioUsuario repositorioUsuario, RepositorioSubasta repositorioSubasta, RepositorioReservaSubasta repositorioReservaSubasta, ServicioNotificacion servicioNotificacion) {
        this.repositorioOferta = repositorioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSubasta = repositorioSubasta;
        this.servicioNotificacion = servicioNotificacion;
        this.servicioPagoInicialSubasta = servicioPagoInicialSubasta;
    }

    @Override
    public Oferta ofertar(Long id, String emailCreador, Float montoOfertado) {

        if (emailCreador == null || emailCreador.isBlank())
            throw new UsuarioNoDefinidoException("Usuario no definido.");
        if (id == null)
            throw new IllegalArgumentException("idSubasta es obligatorio.");
        if (montoOfertado == null)
            throw new IllegalArgumentException("El monto ofertado es obligatorio.");

        // Cargar usuario y subasta
        Usuario usuario = repositorioUsuario.buscar(emailCreador);
        if (usuario == null) throw new RuntimeException("Usuario inexistente.");

        Subasta subasta = repositorioSubasta.obtenerSubasta(id);
        if (subasta == null || subasta.getEstadoSubasta() == null || subasta.getEstadoSubasta() == -2 ) throw new RuntimeException("Subasta inexistente.");
        if (subasta.getEstadoSubasta() == -1) throw new RuntimeException("Subasta cerrada.");

        // Validar que el usuario haya abonado el 10% del valor inicial
        PagoInicialSubasta pago = servicioPagoInicialSubasta.buscarPagoConfirmado(usuario, subasta);
        if (pago == null || !Boolean.TRUE.equals(pago.getPagoConfirmado())) {
            throw new RuntimeException("Debes abonar el 10% del monto inicial para poder ofertar.");
        }
        //validar que el usuario haya abonado el 10% del valor actual
        /*ReservaSubasta reserva = repositorioReservaSubasta.buscarRerservaConfirmada(usuario,subasta);
        if(reserva == null || !reserva.getPagoConfirmado()){
            throw new RuntimeException("Debes abonar el 10% de la subasta actual para poder ofertar.");
        }*/

        Usuario creador = subasta.getCreador();
        if (creador != null && creador.getEmail() != null
                && creador.getEmail().equalsIgnoreCase(emailCreador)) {
            throw new RuntimeException("No es posible que ofertes sobre una Subasta que creaste vos mismo!");
        }

        Float actual = subasta.getPrecioActual() != null ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();
        if (actual == null) throw new RuntimeException("La subasta no tiene precio inicial.");
        if (Float.compare(montoOfertado, actual) <= 0)
            throw new RuntimeException("El monto ofertado debe ser mayor a " + actual);

        Oferta oferta = new Oferta();
        oferta.setSubasta(subasta);
        oferta.setOfertadorID(usuario);
        oferta.setMontoOfertado(montoOfertado);
        oferta.setFechaOferta(LocalDateTime.now());
        repositorioOferta.guardarOferta(oferta);

        subasta.setPrecioActual(montoOfertado);
        repositorioSubasta.actualizar(subasta);

        // Notificar al creador de la subasta
        servicioNotificacion.crearNotificacion(
                subasta.getCreador(),
                "Tu subasta '" + subasta.getTitulo() + "' recibió una nueva oferta de $" + montoOfertado
        );

        // Notificar a los otros participantes (excepto el ofertante actual)
        List<Usuario> otrosOfertantes = repositorioOferta.obtenerOfertantesPorSubasta(subasta, usuario);
        for (Usuario u : otrosOfertantes) {
            servicioNotificacion.crearNotificacion(
                    u,
                    "Han superado tu oferta en la subasta '" + subasta.getTitulo() + "'."
            );
        }

        return oferta;
    }

    @Override
    public Object[] listarOfertasSubastaJSON(Long idSubasta) {
        Object[] returnJSON = repositorioOferta.obtenerOfertasPorSubastaJSON(idSubasta);
        verificarFechasSubasta(idSubasta);
        return returnJSON;
    }

    @Override
    public void verificarFechasSubasta(Long idSubasta) {
        Subasta subasta = repositorioSubasta.obtenerSubasta(idSubasta);
        LocalDateTime fechaAhora = LocalDateTime.now();

        int comparacionFechas = fechaAhora.compareTo(subasta.getFechaFin());
        if (comparacionFechas >= 0) {
            subasta.setEstadoSubasta(-1);
            repositorioSubasta.actualizar(subasta);
        }
    }

    @Override
    public List<Subasta> listarSubastasOfertadasPorUsuario(String emailUsuario) {
        return repositorioOferta.obtenerSubastasOfertadasPorUsuario(emailUsuario);
    }

    @Override
    public Oferta buscarOfertaGanadoraDeSubasta(Long idSubasta) {
        return null;
    }
}
