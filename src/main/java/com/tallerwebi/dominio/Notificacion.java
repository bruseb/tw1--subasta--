package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    private boolean leida = false;
    private LocalDateTime fecha;
    private Long id_subasta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioDestino;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public boolean isLeida() {
        return leida;
    }
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }
    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }
    public Long getId_subasta() {
        return id_subasta;
    }
    public void setId_subasta(Long id_subasta) {
        this.id_subasta = id_subasta;
    }
}
