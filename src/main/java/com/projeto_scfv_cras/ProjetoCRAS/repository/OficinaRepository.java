package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Integer>{
    List<Oficina> findByNomeStartingWithIgnoreCase(String nome, Sort sort);
} 
