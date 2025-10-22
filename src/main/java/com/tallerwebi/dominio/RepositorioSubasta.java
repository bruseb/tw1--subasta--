package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface RepositorioSubasta {
    void guardar(Subasta subasta);
    boolean existeLaSubasta(String titulo, String descripcion, String estadoProducto,Subcategoria subcategoria,Float precioInicial, Usuario creador);
    LocalDateTime obtenerTiempoFin(Integer indicador);
    Subasta obtenerSubasta(Long id);
    @Transactional
    List<Subasta> buscarTodas();
    @Transactional
    List<Subasta> buscarSubasta(String titulo);
    List<Subasta> buscarSubastasPorCreador(String emailCreador);
}
