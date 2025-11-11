package com.tallerwebi.dominio;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="reserva_subasta")
public class ReservaSubasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "subasta_id", nullable = false)
    private Subasta subasta;

    private BigDecimal montoPagado;

    private Boolean pagoConfirmado;

    private Boolean reembolsado;

    private LocalDateTime fechaPago;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Subasta getSubasta() { return subasta; }
    public void setSubasta(Subasta subasta) { this.subasta = subasta; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public Boolean getPagoConfirmado() { return pagoConfirmado; }
    public void setPagoConfirmado(Boolean pagoConfirmado) { this.pagoConfirmado = pagoConfirmado; }

    public Boolean getReembolsado() { return reembolsado; }
    public void setReembolsado(Boolean reembolsado) { this.reembolsado = reembolsado; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}
