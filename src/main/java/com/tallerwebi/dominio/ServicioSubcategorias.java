package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioSubcategorias {
    List<Subcategoria> listarSubcategorias();
    Subcategoria buscarSubcategoriaPorNombreDeRuta(String nombreDeCategoriaEnUrl, String nombreDeSubcategoriaEnUrl);
    List<Subcategoria> listarSubcategoriasSegunCategoriaId(Long idCategoria);
    List<Subcategoria> listarSubcategoriasPopulares();

}
