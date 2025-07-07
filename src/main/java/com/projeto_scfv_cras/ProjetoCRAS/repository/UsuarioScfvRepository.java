package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;

public interface UsuarioScfvRepository extends JpaRepository<UsuarioScfv, Integer>{
    @Query("SELECT u FROM UsuarioScfv u WHERE LOWER(u.nome) LIKE LOWER(CONCAT(:nome, '%')) OR u.nomeResponsavel LIKE CONCAT(:nome, '%')")
    public List<UsuarioScfv> findByNomeOrNomeResponsavel(String nome, Sort sort);

    @Query("""
        SELECT u FROM UsuarioScfv u WHERE u.id NOT IN 
        (SELECT ou.usuario.id FROM OficinaUsuario ou WHERE ou.oficina.id = :idOficina)
        AND LOWER(u.nome) LIKE LOWER(CONCAT(:nome, '%'))
    """)
    List<UsuarioScfv> findUsuariosNaoRegistradosAOficina(Integer idOficina, String nome, Sort sort);
}