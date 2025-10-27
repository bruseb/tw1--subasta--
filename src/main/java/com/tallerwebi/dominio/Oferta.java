package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Oferta {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @OneToOne
        @JoinColumn(name = "usuario_id")
        private Usuario ofertadorID;
        private Float montoOfertado;
        private LocalDateTime fechaOferta;

    @ManyToOne(optional = false)  // varias ofertas pueden pertenecer a una subasta
    @JoinColumn(name = "id_subasta")
    private Subasta subasta;



    public Subasta getSubasta() {
        return subasta;
    }

    public void setSubasta(Subasta subasta) {
        this.subasta = subasta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getOfertadorID() {
        return ofertadorID;
    }

    public void setOfertadorID(Usuario ofertadorID) {
        this.ofertadorID = ofertadorID;
    }

    public LocalDateTime getFechaOferta() {
        return fechaOferta;
    }

    public void setFechaOferta( LocalDateTime fechaOferta) {
        this.fechaOferta = fechaOferta;
    }

    public void setMontoOfertado(Float montoOfertado) {
        this.montoOfertado = montoOfertado;
    }
    public Float getMontoOfertado() {

        return montoOfertado;
    }

}
