package com.projeto_scfv_cras.ProjetoCRAS.service;

import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;

public interface CategoriaOficinaService {
    public void saveCategoria(CategoriaOficina categoria);
    public List<CategoriaOficina> getAllCategorias();
    public CategoriaOficina getCategoriaById(Integer id);
    public void deleteCategoriaById(Integer id);
}
