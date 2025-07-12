package com.projeto_scfv_cras.ProjetoCRAS.service;


import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;

public interface OficinaService {
    public void saveOficina(Oficina oficina);
    public List<Oficina> getAllOficinas();
    public List<Oficina> getOficinaByNome(String nome);
    public Oficina getOficinaById(Integer id);
    public void deleteOficinaById(Integer id);
    public List<Oficina> getOficinasNaoRegistradasAoUsuario(Integer idUsuario, String nome);
    public void deleteByCategoria(CategoriaOficina categoria);
}
