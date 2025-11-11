package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ServicioSubasta {
    void crearSubasta(Subasta subasta,MultipartFile[] imagen, String creador) throws IOException;
    LocalDateTime obtenerTiempoFin(Integer indicador);
    Subasta buscarSubasta(Long idSubasta);
    void eliminarSubasta(Subasta subasta);
    List<Subasta> listarSubastasDelUsuario(String emailCreador);
    List<Subasta> listarSubastasGanadas(String emailCreador);
    List<Subasta> listarSubastasPorCategoriaId(Long idCategoria);
    List<Subasta> listarSubastasPorSubcategoriaId(Long idSubcategoria);
}
