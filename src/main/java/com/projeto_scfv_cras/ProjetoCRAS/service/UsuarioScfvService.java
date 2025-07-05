package com.projeto_scfv_cras.ProjetoCRAS.service;

import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

public interface UsuarioScfvService {
    public void saveUsuario(UsuarioScfv usuario);
    List<UsuarioScfv> getAllUsuarios();
    List<UsuarioScfv> getUsuarioByNomeOrNomeResponsavel(String nome);
    UsuarioScfv getUsuarioById(Integer id);
    void deleteUsuarioById(Integer id);
}
