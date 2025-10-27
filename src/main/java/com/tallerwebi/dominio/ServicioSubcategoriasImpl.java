package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("servicioSubcategoriasImpl")
@Transactional
public class ServicioSubcategoriasImpl implements ServicioSubcategorias {

    private final RepositorioSubcategorias repositorioSubcategorias;

    @Autowired
    public ServicioSubcategoriasImpl(RepositorioSubcategorias repositorioSubcategorias) {
        this.repositorioSubcategorias = repositorioSubcategorias;
    }

    @Override
    public List<Subcategoria> listarSubcategorias() {
        return repositorioSubcategorias.listarTodasLasSubcategorias();
    }

    @Override
    public Subcategoria buscarSubcategoriaPorNombreDeRuta(String nombreDeCategoriaEnUrl, String nombreDeSubcategoriaEnUrl) {
        return repositorioSubcategorias.buscarSubcategoriaPorNombreDeRuta(nombreDeCategoriaEnUrl, nombreDeSubcategoriaEnUrl);
    }

    @Override
    public List<Subcategoria> listarSubcategoriasSegunCategoriaId(Long idCategoria) {
        return repositorioSubcategorias.listarSubcategoriasDeCategoriaSeleccionadaPorId(idCategoria);
    }

    @Override
    public List<Subcategoria> listarSubcategoriasPopulares() {
        return repositorioSubcategorias.listarSubcategoriasPopulares();
    }
}
