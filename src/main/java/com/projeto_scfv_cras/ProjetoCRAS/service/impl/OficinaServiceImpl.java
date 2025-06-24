package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;

@Service
public class OficinaServiceImpl implements OficinaService{
    @Autowired 
    private OficinaRepository oficinaRepository;

    @Override
    public void saveOficina(Oficina oficina){
        oficinaRepository.save(oficina);
    }
}