package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.repository.CategoriaOficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.CategoriaOficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;

@Service
public class CategoriaOficinaServiceImpl implements CategoriaOficinaService{
    @Autowired
    private CategoriaOficinaRepository categoriaOficinaRepository;

    @Autowired
    private OficinaService oficinaService;

    @Override
    public void saveCategoria(CategoriaOficina categoria){
        categoriaOficinaRepository.save(categoria);
    }

    @Override
    public List<CategoriaOficina> getAllCategorias(){
        return categoriaOficinaRepository.findAll();
    }

    @Override
    public CategoriaOficina getCategoriaById(Integer id){
        Optional<CategoriaOficina> optional = categoriaOficinaRepository.findById(id);
        CategoriaOficina categoria = null;

        if(optional.isPresent()){
            categoria = optional.get();
        } else{
            throw new RuntimeException("Categoria não encontrada para o id " + id);
        }
        return categoria;
    }

    @Override
    @Transactional
    public void deleteCategoriaById(Integer id){
        CategoriaOficina categoria = getCategoriaById(id);
        oficinaService.deleteByCategoria(categoria);
        categoriaOficinaRepository.deleteById(id);
    }
}
