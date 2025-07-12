package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.OficinaUsuario;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaUsuarioRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;

@Service
public class OficinaUsuarioServiceImpl implements OficinaUsuarioService{
    @Autowired
    private OficinaUsuarioRepository oficinaUsuarioRepository;

    @Autowired
    private OficinaRepository oficinaRepository;

    @Override
    public boolean registrarUsuarioEmOficina(Oficina oficina, UsuarioScfv usuario){
        if(oficina.getVagasOcupadas() < oficina.getQtdVagas()){
            OficinaUsuario ou = new OficinaUsuario();
            ou.setOficina(oficina);
            ou.setUsuario(usuario);
            oficinaUsuarioRepository.save(ou);
            oficina.setVagasOcupadas(oficina.getVagasOcupadas() + 1);
            oficinaRepository.save(oficina);
            return true;
        }
        return false;
    }

    @Override
    public List<OficinaUsuario> getByUsuario(UsuarioScfv usuario){
        return oficinaUsuarioRepository.findByUsuario(usuario);
    }

    @Override
    public List<OficinaUsuario> getByOficina(Oficina oficina){
        return oficinaUsuarioRepository.findByOficina(oficina);
    }

    @Override
    @Transactional
    public void deleteByUsuarioAndOficina(Oficina oficina, UsuarioScfv usuario){
        oficinaUsuarioRepository.deleteByOficinaAndUsuario(oficina, usuario);
        oficina.setVagasOcupadas(oficina.getVagasOcupadas() - 1);
        oficinaRepository.save(oficina);
    }
}
