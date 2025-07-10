package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaUsuarioRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.UsuarioScfvRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

@Service
public class UsuarioScfvServiceImpl implements UsuarioScfvService{
    @Autowired
    private UsuarioScfvRepository usuarioScfvRepository;

    @Autowired
    private OficinaUsuarioRepository oficinaUsuarioRepository;

    @Override
    public void saveUsuario(UsuarioScfv usuario){
        usuarioScfvRepository.save(usuario);
    }

    @Override
    public List<UsuarioScfv> getAllUsuarios(){
        return usuarioScfvRepository.findAll(Sort.by("nome").ascending());
    }

    @Override
    public UsuarioScfv getUsuarioById(Integer id){
        Optional<UsuarioScfv> optional = usuarioScfvRepository.findById(id);
        UsuarioScfv usuario = null;

        if(optional.isPresent()){
            usuario = optional.get();
        } else{
            throw new RuntimeException("Usuário não encontrado para o id " + id);
        }
        return usuario;
    }

    @Override
    @Transactional
    public void deleteUsuarioById(Integer id){
        UsuarioScfv usuario = getUsuarioById(id);
        oficinaUsuarioRepository.deleteByUsuario(usuario);
        usuarioScfvRepository.delete(usuario);
    }

    @Override
    public List<UsuarioScfv> getUsuarioByNomeOrNomeResponsavel(String nome){
        return usuarioScfvRepository.findByNomeOrNomeResponsavel(nome, Sort.by("nome").ascending());
    }

    @Override
    public List<UsuarioScfv> getUsuariosNaoRegistradosAOficina(Integer idOficina, String nome){
        return usuarioScfvRepository.findUsuariosNaoRegistradosAOficina(idOficina, nome, Sort.by("nome").ascending());
    }
}
