package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioSubcategorias {
    List<Subcategoria> listarTodasLasSubcategorias();
    Subcategoria buscarSubcategoriaPorNombreDeRuta(String nombreDeCategoriaEnUrl, String nombreDeSubcategoriaEnUrl);
    List<Subcategoria> listarSubcategoriasDeCategoriaSeleccionadaPorId(Long idCategoria);
    List<Subcategoria> listarSubcategoriasPopulares();
}
