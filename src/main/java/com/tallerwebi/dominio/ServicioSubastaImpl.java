package com.tallerwebi.dominio;

import com.tallerwebi.dominio.RepositorioSubasta;
import com.tallerwebi.dominio.ServicioSubasta;
import com.tallerwebi.dominio.Subasta;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.exception.UsuarioNoDefinidoException;
import com.tallerwebi.dominio.RepositorioSubasta;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service("servicioSubasta")
@Transactional
public class ServicioSubastaImpl implements ServicioSubasta {

    private RepositorioSubasta repositorioSubasta;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioOferta repositorioOferta;
    private ServicioNotificacion servicioNotificacion;
    private PerspectiveApi perspectiveApi;

    @Autowired
    public ServicioSubastaImpl(RepositorioSubasta repositorioSubasta, RepositorioUsuario repositorioUsuario, RepositorioOferta repositorioOferta, ServicioNotificacion servicioNotificacion, PerspectiveApi perspectiveApi) {
        this.repositorioSubasta     = repositorioSubasta;
        this.repositorioUsuario     = repositorioUsuario;
        this.repositorioOferta      = repositorioOferta;
        this.servicioNotificacion   = servicioNotificacion;
        this.perspectiveApi         = perspectiveApi;
    }

    @Override
    public void crearSubasta(Subasta subasta,MultipartFile[] imagenes, String emailCreador) throws IOException {
        List<Imagen> imagenesList  = new ArrayList<>();

        if(emailCreador == null || emailCreador.isEmpty()){
            throw new UsuarioNoDefinidoException("Usuario no definido.");
        }

        if(imagenes == null || imagenes.length == 0 || imagenes[0] == null || imagenes[0].isEmpty()){
            throw new RuntimeException("Imagen no definida.");
        }

        if(     subasta.getEstadoSubasta() != 0 &&
                subasta.getEstadoSubasta() != 1 &&
                subasta.getEstadoSubasta() != 2 &&
                subasta.getEstadoSubasta() != 3 &&
                subasta.getEstadoSubasta() != 4){
            throw new RuntimeException("Estado de subasta no valido.");
        }

        if(subasta.getSubcategoria() == null || subasta.getSubcategoria().getId() == null || subasta.getSubcategoria().getId() == -1){
            throw new RuntimeException("Categoria no definida.");
        }

        Usuario usuario = repositorioUsuario.buscar(emailCreador);
        if (usuario == null) {
            throw new RuntimeException("Usuario inexistente.");
        }
        if(subasta.getPrecioInicial()==null || subasta.getPrecioInicial() < 0){
            throw new IllegalArgumentException ("El monto inicial no puede ser negativo.");
        }

        subasta.setCreador(repositorioUsuario.buscar(emailCreador));
        subasta.setFechaInicio();
        subasta.setFechaFin(obtenerTiempoFin(subasta.getEstadoSubasta()));   //Subasta en curso
        subasta.setEstadoSubasta(10);

        boolean yaExiste = repositorioSubasta.existeLaSubasta(subasta.getTitulo(), subasta.getDescripcion(),subasta.getEstadoProducto(), subasta.getSubcategoria(),subasta.getPrecioInicial() ,subasta.getCreador());


        if(yaExiste){
            throw new RuntimeException("Ya existe una subasta con los mismos datos.");
        }

        try{
            if(perspectiveApi.esTextoOfensivo(subasta.getTitulo()) || perspectiveApi.esTextoOfensivo(subasta.getDescripcion())){
                throw new RuntimeException("El título o la descripción contienen lenguaje ofensivo.");
            }

        } catch (IOException | InterruptedException e){
            throw new RuntimeException("Error al analizar el contenido con Perspective API.", e);
        }

        for (MultipartFile i : imagenes) {
            if(i.getContentType() == null || !i.getContentType().startsWith("image/")){
                throw new RuntimeException("El archivo debe ser una imagen.");
            }
            Imagen temp = new Imagen();
            temp.setImagen(Base64.getEncoder().encodeToString(i.getBytes()));
            temp.setSubastas(subasta);
            imagenesList.add(temp);
        }
        subasta.setImagenes(imagenesList);

        repositorioSubasta.guardar(subasta);
    }

    @Override
    public LocalDateTime obtenerTiempoFin(Integer indicador){
        LocalDateTime temp = LocalDateTime.now();
        LocalDateTime result;
        switch(indicador){
            case 0:
                result = temp.plusHours(12);    //Express
                break;
            case 1:
                result = temp.plusDays(1);      //Rapido
                break;
            case 2:
                result = temp.plusDays(3);      //Normal
                break;
            case 3:
                result = temp.plusDays(7);      //Prolongado
                break;
            case 4:
                result = temp.plusMinutes(3);   //Ultra Express
                break;
            default:
                result = temp;
        }
        return result;
    }


    @Override
    public Subasta buscarSubasta(Long idSubasta) {return repositorioSubasta.obtenerSubasta(idSubasta);}

    @Override
    public List<Subasta> listarSubastasDelUsuario(String emailCreador) {
        if (emailCreador == null || emailCreador.isEmpty()) {
            throw new RuntimeException("El email del usuario no puede ser nulo o vacío.");
        }
        return repositorioSubasta.buscarSubastasPorCreador(emailCreador);
    }


    @Override
    public List<Subasta> listarSubastasPorCategoriaId(Long idCategoria){
        return repositorioSubasta.buscarSubastasPorCategoriaId(idCategoria);
    }

    @Override
    public List<Subasta> listarSubastasPorSubcategoriaId(Long idSubcategoria){
        return repositorioSubasta.buscarSubastasPorSubcategoriaId(idSubcategoria);
    }

    @Override
    public List<Subasta> listarSubastasGanadas(String emailCreador){
        return repositorioSubasta.buscarSubastasGanadas(emailCreador);
    }

    @Override
    public void eliminarSubasta(Subasta subasta) {
        //Estado Subasta = -2 => Subasta eliminada. No debe aparecer en las busquedas y se trata igual que si no existe en la DDBB
        subasta.setEstadoSubasta(-2);
        //NOTIFICACION
        List<Usuario> ofertantes = repositorioOferta.obtenerOfertantesPorSubasta(subasta, subasta.getCreador());
        for (Usuario u : ofertantes) {
            servicioNotificacion.crearNotificacion(u,"La subasta '" + subasta.getTitulo() + "' ha sido borrada. Tus ofertas fueron canceladas.", subasta.getId());
        }
        //Actualizar Subasta
        repositorioSubasta.actualizar(subasta);
    }

}
