package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

public interface UsuarioScfvRepository extends JpaRepository<UsuarioScfv, Integer>{
    @Query("SELECT u FROM UsuarioScfv u WHERE u.nome LIKE CONCAT(:nome, '%') OR u.nomeResponsavel LIKE CONCAT(:nome, '%')")
    public List<UsuarioScfv> findByNomeOrNomeResponsavel(String nome);
}
