package com.tallerwebi.dominio;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idSubasta;
    private String email;
    private Float montoAbonado;

    // Estado de la transacción (2: Pagado/Confirmado, 1: Pendiente, 0: Fallido)
    private Integer estado;

    public Pago() {}

    public Pago(Long id, Long idSubasta, String email, Float montoAbonado, Integer estado) {
        this.id = id;
        this.idSubasta = idSubasta;
        this.email = email;
        this.montoAbonado = montoAbonado;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSubasta() {
        return idSubasta;
    }

    public void setIdSubasta(Long idSubasta) {
        this.idSubasta = idSubasta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getMontoAbonado() {
        return montoAbonado;
    }

    public void setMontoAbonado(Float montoAbonado) {
        this.montoAbonado = montoAbonado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
