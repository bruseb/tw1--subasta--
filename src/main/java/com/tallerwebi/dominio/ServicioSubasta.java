package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioSubasta {
    void crearSubasta(Subasta subasta,MultipartFile imagen, String creador) throws IOException;
    Subasta buscarSubasta(Long idSubasta);
    List<Subasta> listarSubastasDelUsuario(String emailCreador);
    List<Subasta> listarSubastasPorCategoriaId(Long idCategoria);
    List<Subasta> listarSubastasPorSubcategoriaId(Long idSubcategoria);
}
