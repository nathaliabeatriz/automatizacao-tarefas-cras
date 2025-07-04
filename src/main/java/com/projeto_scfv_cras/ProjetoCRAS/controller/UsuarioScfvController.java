package com.projeto_scfv_cras.ProjetoCRAS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

import jakarta.validation.Valid;

@Controller
public class UsuarioScfvController {
    @Autowired
    private UsuarioScfvService usuarioScfvService;

    @GetMapping("usuarios")
    public String menuOficinas(){
        return "usuario_scfv/index";
    }

    @GetMapping("usuarios/create")
    public String create(Model model){
        model.addAttribute("usuario", new UsuarioScfv());
        return "usuario_scfv/create";
    }

    @PostMapping("usuarios/save")
    public String salvarOficina(@ModelAttribute("usuario") @Valid UsuarioScfv usuario, BindingResult result){
        if(result.hasErrors()){
            if(usuario.getId() != null){
                return "usuario_scfv/edit";
            }
            return "usuario_scfv/create";
        }
        usuarioScfvService.saveUsuario(usuario);
        return "usuario_scfv/index";
    }
}
