package com.tallerwebi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subcategorias")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String nombreEnUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnore
    private Categoria categoria;

    @OneToMany(mappedBy = "subcategoria", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subasta> subastas = new ArrayList<>();

    @Transient
    @JsonIgnore
    public Subasta getSubastaAleatoria() {
        if (subastas == null || subastas.isEmpty()) {
            return null;
        }
        int indiceAleatorio = (int) (Math.random() * subastas.size());
        return subastas.get(indiceAleatorio);
    }

    @Transient
    public Long getIdSubastaAleatoria() {
        Subasta aleatoria = getSubastaAleatoria();
        return (aleatoria != null) ? aleatoria.getId() : null;
    }

    public Long getId() {return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEnUrl() {
        return nombreEnUrl;
    }
    public void setNombreEnUrl(String nombreEnUrl) {
        this.nombreEnUrl = nombreEnUrl;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Subasta> getSubastas() {
        return subastas;
    }
    public void setSubastas(List<Subasta> subastas) {
        this.subastas = subastas;
    }
}

