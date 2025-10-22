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

    @Autowired
    public ServicioSubastaImpl(RepositorioSubasta repositorioSubasta, RepositorioUsuario repositorioUsuario, RepositorioCategorias repositorioCategorias) {
        this.repositorioSubasta     = repositorioSubasta;
        this.repositorioUsuario     = repositorioUsuario;
        this.repositorioCategorias  = repositorioCategorias;
    }

    @Override
    public void crearSubasta(Subasta subasta,MultipartFile imagen, String creador) throws IOException {
        if(creador == null || creador.isEmpty()){
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

        Usuario usuario = repositorioUsuario.buscar(creador);
        if (usuario == null) {
            throw new RuntimeException("Usuario inexistente.");
        }

        subasta.setCreador(repositorioUsuario.buscar(creador));
        subasta.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
        subasta.setFechaInicio();
        subasta.setFechaFin(repositorioSubasta.obtenerTiempoFin(subasta.getEstadoSubasta()));   //Subasta en curso
        subasta.setEstadoSubasta(10);
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

}
