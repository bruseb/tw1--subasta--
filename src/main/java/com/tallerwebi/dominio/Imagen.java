package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String imagen;
    @ManyToOne(optional = false)
    @JoinColumn(name="subasta_id", nullable=false)
    private Subasta subasta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Subasta getSubastas() { return subasta; }
    public void setSubastas(Subasta subastas) { this.subasta = subastas; }
}
