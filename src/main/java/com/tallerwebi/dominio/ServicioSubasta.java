package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioSubasta {
    void crearSubasta(Subasta subasta,MultipartFile imagen, String creador) throws IOException;
    List<Categoria> listarCategoriasDisponibles();
    Subasta buscarSubasta(Long idSubasta);
    List<Subasta> listarSubastasDelUsuario(String emailCreador);
}
