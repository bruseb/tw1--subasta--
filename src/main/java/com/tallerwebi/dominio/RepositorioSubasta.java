package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositorioSubasta {
    void guardar(Subasta subasta);
    boolean existeLaSubasta(String titulo, String descripcion, String estadoProducto,Subcategoria subcategoria,Float precioInicial, Usuario creador);
    LocalDateTime obtenerTiempoFin(Integer indicador);
    Subasta obtenerSubasta(Long id);
    List<Subasta> buscarSubasta(String titulo);
    List<Subasta> buscarSubastasPorCreador(String emailCreador);
    List<Subasta> buscarSubastasPorCategoriaId(Long idCategoria);
    List<Subasta> buscarSubastasPorSubcategoriaId(Long idSubcategoria);
}
