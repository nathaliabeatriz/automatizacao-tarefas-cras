package com.projeto_scfv_cras.ProjetoCRAS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Integer>{
    
}
