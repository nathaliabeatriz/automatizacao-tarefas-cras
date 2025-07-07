package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaUsuarioRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;

@Service
public class OficinaServiceImpl implements OficinaService{
    @Autowired 
    private OficinaRepository oficinaRepository;

    @Autowired
    private OficinaUsuarioRepository oficinaUsuarioRepository;

    @Override
    public void saveOficina(Oficina oficina){
        oficinaRepository.save(oficina);
    }

    @Override
    public List<Oficina> getAllOficinas(){
        return oficinaRepository.findAll(Sort.by("nome").ascending());
    }

    @Override
    public List<Oficina> getOficinaByNome(String nome){
        return oficinaRepository.findByNomeStartingWithIgnoreCase(nome, Sort.by("nome").ascending());
    }

    @Override
    public Oficina getOficinaById(Integer id){
        Optional<Oficina> optional = oficinaRepository.findById(id);
        Oficina oficina = null;

        if(optional.isPresent()){
            oficina = optional.get();
        } else{
            throw new RuntimeException("Oficina não encontrada para o id " + id);
        }
        return oficina;
    }

    @Override
    @Transactional
    public void deleteOficinaById(Integer id){
        Oficina oficina = getOficinaById(id);
        oficinaUsuarioRepository.deleteByOficina(oficina);
        oficinaRepository.delete(oficina);
    }

    @Override
    public List<Oficina> getOficinasNaoRegistradasAoUsuario(Integer idUsuario, String nome){
        return oficinaRepository.findOficinasNaoRegistradasAoUsuario(idUsuario, nome, Sort.by("nome").ascending());
    }
}