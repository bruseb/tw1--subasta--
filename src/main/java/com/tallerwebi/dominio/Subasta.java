package com.tallerwebi.dominio;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Subasta")
public class Subasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;
    private String titulo;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="subcategoria_id", nullable=false)
    private Subcategoria subcategoria;

    private String estadoProducto;
    private Float precioInicial;
    private Float precioActual; //Dependiendo de como aplicaramos el de subastar, esto se puede eliminar por un SELECT
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin; //  Express -> Inicio + 12hr | Rapido -> Inicio + 24 hrs || Normal -> Inicio + 72 hrs || Prolongado -> Inicio + 168 hrs
    private Integer estadoSubasta;  //  10 = En curso | -1 = Cerrada
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subasta",  fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Imagen> imagenes = new ArrayList<>();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getCreador() { return creador; }
    public void setCreador(Usuario creador) { this.creador = creador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Subcategoria getSubcategoria() {  return subcategoria; }
    public void setSubcategoria(Subcategoria subcategoria) { this.subcategoria = subcategoria; }

    public String getEstadoProducto() { return estadoProducto; }
    public void setEstadoProducto(String estadoProducto) { this.estadoProducto = estadoProducto; }

    public Float getPrecioInicial() { return precioInicial; }
    public void setPrecioInicial(Float precioInicial) {this.precioInicial = precioInicial; }

    public Float getPrecioActual() { return precioActual; }
    public void setPrecioActual(Float precioActual) {this.precioActual = precioActual; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio() {this.fechaInicio = LocalDateTime.now(); }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) {this.fechaFin = fechaFin; }

    public Integer getEstadoSubasta() { return estadoSubasta; }
    public void setEstadoSubasta(Integer estadoSubasta) { this.estadoSubasta = estadoSubasta; }

    public List<Imagen> getImagenes() { return imagenes; }
    public void setImagenes(List<Imagen> imagenes) { this.imagenes = imagenes; }

}
