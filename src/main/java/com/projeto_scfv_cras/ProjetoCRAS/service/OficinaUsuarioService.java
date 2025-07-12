package com.projeto_scfv_cras.ProjetoCRAS.service;

import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.OficinaUsuario;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

public interface OficinaUsuarioService {
    public boolean registrarUsuarioEmOficina(Oficina oficina, UsuarioScfv usuario);
    public List<OficinaUsuario> getByUsuario(UsuarioScfv usuario);
    public List<OficinaUsuario> getByOficina(Oficina oficina);
    public void deleteByUsuarioAndOficina(Oficina oficina, UsuarioScfv usuario);
}
