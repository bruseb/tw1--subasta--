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
import java.util.Base64;
import java.util.List;

@Service("servicioSubasta")
@Transactional
public class ServicioSubastaImpl implements ServicioSubasta {

    private RepositorioSubasta repositorioSubasta;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCategorias repositorioCategorias;
    private PerspectiveApi perspectiveApi;

    @Autowired
    public ServicioSubastaImpl(RepositorioSubasta repositorioSubasta, RepositorioUsuario repositorioUsuario, RepositorioCategorias repositorioCategorias, PerspectiveApi perspectiveApi) {
        this.repositorioSubasta     = repositorioSubasta;
        this.repositorioUsuario     = repositorioUsuario;
        this.repositorioCategorias  = repositorioCategorias;
        this.perspectiveApi = perspectiveApi;
    }

    @Override
    public void crearSubasta(Subasta subasta,MultipartFile imagen, String emailCreador) throws IOException {

        if(emailCreador == null || emailCreador.isEmpty()){
            throw new UsuarioNoDefinidoException("Usuario no definido.");
        }

        if(imagen == null || imagen.isEmpty()){
            throw new RuntimeException("Imagen no definida.");
        }

        if (imagen.getContentType() == null || !imagen.getContentType().startsWith("image/")) {
            throw new RuntimeException("El archivo debe ser una imagen.");
        }

        if(     subasta.getEstadoSubasta() != 0 &&
                subasta.getEstadoSubasta() != 1 &&
                subasta.getEstadoSubasta() != 2 &&
                subasta.getEstadoSubasta() != 3){
            throw new RuntimeException("Estado de subasta no valido.");
        }

        Usuario usuario = repositorioUsuario.buscar(emailCreador);
        if (usuario == null) {
            throw new RuntimeException("Usuario inexistente.");
        }

        subasta.setCreador(usuario);
        subasta.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
        subasta.setFechaInicio();
        subasta.setFechaFin(repositorioSubasta.obtenerTiempoFin(subasta.getEstadoSubasta()));   //Subasta en curso
        subasta.setEstadoSubasta(10);

        boolean yaExiste = repositorioSubasta.existeLaSubasta(subasta.getTitulo(), subasta.getDescripcion(),subasta.getEstadoProducto(), subasta.getSubcategoria(),subasta.getPrecioInicial() ,subasta.getCreador());


        if(yaExiste){
            throw new RuntimeException("Ya exite una subasta con los mismos datos");
        }

        try{
            if(perspectiveApi.esTextoOfensivo(subasta.getTitulo()) || perspectiveApi.esTextoOfensivo(subasta.getDescripcion())){
                throw new RuntimeException("El título o la descripción contienen lenguaje ofensivo.");
            }

        } catch (IOException | InterruptedException e){
            throw new RuntimeException("Error al analizar el contenido con Perspective API", e);
        }

        repositorioSubasta.guardar(subasta);
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

}
