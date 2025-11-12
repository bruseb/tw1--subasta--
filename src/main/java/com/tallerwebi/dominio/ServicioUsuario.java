package com.tallerwebi.dominio;

public interface ServicioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    void modificar(Usuario usuario);
}

