package com.projeto_scfv_cras.ProjetoCRAS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;

public interface CategoriaOficinaRepository extends JpaRepository<CategoriaOficina, Integer>{
    CategoriaOficina findByNome(String nome);
}
