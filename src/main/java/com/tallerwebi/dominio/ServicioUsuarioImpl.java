package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
    public class ServicioUsuarioImpl implements ServicioUsuario {

        private final RepositorioUsuario repositorioUsuario;

        @Autowired
        public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
            this.repositorioUsuario = repositorioUsuario;
        }

        @Override
        @Transactional(readOnly = true)
        public Usuario buscarUsuario(String email, String password) {
            return repositorioUsuario.buscarUsuario(email, password);
        }

        @Override
        @Transactional
        public void guardar(Usuario usuario) {
            repositorioUsuario.guardar(usuario);
        }

        @Override
        @Transactional(readOnly = true)
        public Usuario buscarPorEmail(String email) {
            return repositorioUsuario.buscarPorEmail(email);
        }

        @Override
        @Transactional
        public void modificar(Usuario usuario) {
            repositorioUsuario.modificar(usuario);
        }
}
