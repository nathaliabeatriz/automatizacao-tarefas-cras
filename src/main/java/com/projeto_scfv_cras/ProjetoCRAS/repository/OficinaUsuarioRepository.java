package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.OficinaUsuario;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

@Repository
public interface OficinaUsuarioRepository extends JpaRepository<OficinaUsuario, Integer>{
    List<OficinaUsuario> findByUsuario(UsuarioScfv usuario);
    List<OficinaUsuario> findByOficina(Oficina oficina);
    void deleteByOficinaAndUsuario(Oficina oficina, UsuarioScfv usuario);

    void deleteByUsuario(UsuarioScfv usuario);
    void deleteByOficina(Oficina oficina);
}
 