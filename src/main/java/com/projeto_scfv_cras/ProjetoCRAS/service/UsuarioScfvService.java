package com.projeto_scfv_cras.ProjetoCRAS.service;

import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

public interface UsuarioScfvService {
    public void saveUsuario(UsuarioScfv usuario);
    public List<UsuarioScfv> getAllUsuarios();
    public List<UsuarioScfv> getUsuarioByNomeOrNomeResponsavel(String nome);
    public UsuarioScfv getUsuarioById(Integer id);
    public void deleteUsuarioById(Integer id);
    public List<UsuarioScfv> getUsuariosNaoRegistradosAOficina(Integer idOficina, String nome);
}
