package com.tallerwebi.dominio;

import com.tallerwebi.exception.UsuarioNoDefinidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("servicioOferta")
@Transactional
public class ServicioOfertaImpl implements ServicioOferta{

    private final RepositorioOferta repositorioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSubasta repositorioSubasta;
    private final RepositorioReservaSubasta repositorioReservaSubasta;
    private final ServicioNotificacion servicioNotificacion;

    @Autowired
    public ServicioOfertaImpl(RepositorioOferta repositorioOferta, RepositorioUsuario repositorioUsuario, RepositorioSubasta repositorioSubasta, RepositorioReservaSubasta repositorioReservaSubasta, ServicioNotificacion servicioNotificacion) {
        this.repositorioOferta = repositorioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSubasta = repositorioSubasta;
        this.repositorioReservaSubasta = repositorioReservaSubasta;
        this.servicioNotificacion = servicioNotificacion;
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

        Subasta subasta = repositorioSubasta.obtenerSubasta(id);
        if (subasta == null) throw new RuntimeException("Subasta inexistente.");
        if (subasta.getEstadoSubasta() == -1) throw new RuntimeException("Subasta cerrada.");

        //validar que el usuario haya abonado el 10% del valor actual
        /*ReservaSubasta reserva = repositorioReservaSubasta.buscarRerservaConfirmada(usuario,subasta);
        if(reserva == null || !reserva.getPagoConfirmado()){
            throw new RuntimeException("Debes abonar el 10% de la subasta actual para poder ofertar.");
        }*/

        //Validacion antiofertante
        Usuario creador = subasta.getCreador();
        if(creador != null && creador.getEmail() !=null
        && creador.getEmail().equalsIgnoreCase(emailCreador)){
            throw new RuntimeException("No es posible que ofertes sobre una Subasta que creaste vos mismo!");
        }


        // Regla de negocio (Float)
        Float actual = subasta.getPrecioActual() != null ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();
        if (actual == null) throw new RuntimeException("La subasta no tiene precio inicial.");
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
        repositorioSubasta.actualizar(subasta);

        // Notificar a los otros participantes (excepto el ofertante actual)
        List<Usuario> otrosOfertantes = repositorioOferta.obtenerOfertantesPorSubasta(subasta, usuario);
        for (Usuario u : otrosOfertantes) {
            servicioNotificacion.crearNotificacion(
                    u,"Han superado tu oferta en la subasta '" + subasta.getTitulo() + "'.", subasta.getId()
            );
        }

        return oferta;
    }

    @Override
    public Object[] listarOfertasSubastaJSON(Long idSubasta){
        Object[] returnJSON = repositorioOferta.obtenerOfertasPorSubastaJSON(idSubasta);
        verificarFechasSubasta(idSubasta);
        return returnJSON;
    }

    @Override
    public void verificarFechasSubasta(Long idSubasta) {
        Subasta subasta = repositorioSubasta.obtenerSubasta(idSubasta);
        LocalDateTime fechaAhora =  LocalDateTime.now();

        int comparacionFechas = fechaAhora.compareTo(subasta.getFechaFin());//Si es un numero negativo, fechaAhora es anterior a FechaFin
        if(comparacionFechas >= 0){
            //Cerrar Subasta
            subasta.setEstadoSubasta(-1);
            repositorioSubasta.actualizar(subasta);
            //Notificacion al ganador
            Oferta ofertaGanadora = repositorioOferta.obtenerMayorOfertaPorSubasta(idSubasta);
            if (ofertaGanadora != null) {
                Usuario ganador = ofertaGanadora.getOfertadorID();
                String mensaje = "¡Felicitaciones! Ganaste la subasta '" + subasta.getTitulo() + "'.";
                servicioNotificacion.crearNotificacion(ganador, mensaje, subasta.getId());
            }
            //Notificacion a los perdedores
            if (ofertaGanadora != null) {
                List<Usuario> perdedores = repositorioOferta.obtenerOfertantesPorSubasta(subasta, ofertaGanadora.getOfertadorID());
                for (Usuario perdedor : perdedores) {
                    String mensaje = "Perdiste en la subasta '" + subasta.getTitulo() + "'.";
                    servicioNotificacion.crearNotificacion(perdedor, mensaje, subasta.getId());
                }
            }
        }
    }

    @Override
    public List<Subasta> listarSubastasOfertadasPorUsuario(String emailUsuario) {
        return repositorioOferta.obtenerSubastasOfertadasPorUsuario(emailUsuario);
    }
}
