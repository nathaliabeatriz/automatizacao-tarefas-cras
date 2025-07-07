package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Integer>{
    List<Oficina> findByNomeStartingWithIgnoreCase(String nome, Sort sort);

    @Query("""
        SELECT o FROM Oficina o WHERE o.id NOT IN 
        (SELECT ou.oficina.id FROM OficinaUsuario ou WHERE ou.usuario.id = :idUsuario)
        AND LOWER(o.nome) LIKE LOWER(CONCAT(:nome, '%'))
    """)
    List<Oficina> findOficinasNaoRegistradasAoUsuario(Integer idUsuario, String nome, Sort sort);
} 
