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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service("servicioSubasta")
@Transactional
public class ServicioSubastaImpl implements ServicioSubasta {

    private RepositorioSubasta repositorioSubasta;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCategorias repositorioCategorias;

    @Autowired
    public ServicioSubastaImpl(RepositorioSubasta repositorioSubasta,  RepositorioUsuario repositorioUsuario, RepositorioCategorias repositorioCategorias) {
        this.repositorioSubasta     = repositorioSubasta;
        this.repositorioUsuario     = repositorioUsuario;
        this.repositorioCategorias  = repositorioCategorias;
    }

    @Override
    public void crearSubasta(Subasta subasta,MultipartFile[] imagenes, String creador) throws IOException {
        List<Imagen> imagenesList  = new ArrayList<>();

        if(creador == null || creador.isEmpty()){
            throw new UsuarioNoDefinidoException("Usuario no definido.");
        }

        if(imagenes == null || imagenes.length == 0 || imagenes[0] == null || imagenes[0].isEmpty()){
            throw new RuntimeException("Imagen no definida.");
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
        subasta.setFechaInicio();
        subasta.setFechaFin(repositorioSubasta.obtenerTiempoFin(subasta.getEstadoSubasta()));   //Subasta en curso
        subasta.setEstadoSubasta(10);

        boolean yaExiste = repositorioSubasta.existeLaSubasta(subasta.getTitulo(), subasta.getDescripcion(),subasta.getEstadoProducto(), subasta.getCategoria(),subasta.getPrecioInicial() ,subasta.getCreador());

        if(yaExiste){
            throw new RuntimeException("Ya exite una subasta con los mismos datos");
        }
        for (MultipartFile i : imagenes) {
            Imagen temp = new Imagen();
            temp.setImagen(Base64.getEncoder().encodeToString(i.getBytes()));
            temp.setSubastas(subasta);
            imagenesList.add(temp);
        }
        subasta.setImagenes(imagenesList);

        repositorioSubasta.guardar(subasta);
    }

    @Override
    public List<Categoria> listarCategoriasDisponibles() {
        return repositorioCategorias.listarCategorias();
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
