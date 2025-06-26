package com.projeto_scfv_cras.ProjetoCRAS.service;


import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;

public interface OficinaService {
    public void saveOficina(Oficina oficina);
    List<Oficina> getAllOficinas();
    List<Oficina> getOficinaByNome(String nome);
    Oficina getOficinaById(Integer id);
    void deleteOficinaById(Integer id);
}
