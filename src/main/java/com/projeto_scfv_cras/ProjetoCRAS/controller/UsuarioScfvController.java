package com.projeto_scfv_cras.ProjetoCRAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

import jakarta.validation.Valid;

@Controller
public class UsuarioScfvController {
    @Autowired
    private UsuarioScfvService usuarioScfvService;
    @Autowired
    private OficinaUsuarioService oficinaUsuarioService;

    @GetMapping("usuarios")
    public String menuUsuarios(){
        return "usuario_scfv/index";
    }

    @GetMapping("usuarios/create")
    public String create(Model model){
        model.addAttribute("usuario", new UsuarioScfv());
        return "usuario_scfv/create";
    }

    @GetMapping("usuarios/buscar")
    public String buscarUsuarios(@RequestParam(required = false) String nome, Model model){
        List<UsuarioScfv> usuarios;
        if(nome != null && !nome.isBlank()){
            usuarios = usuarioScfvService.getUsuarioByNomeOrNomeResponsavel(nome);
        } else{
            usuarios = usuarioScfvService.getAllUsuarios();
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("qtdResultados", usuarios.size());
        return "usuario_scfv/buscar";
    }

    @PostMapping("usuarios/save")
    public String salvarUsuario(@ModelAttribute("usuario") @Valid UsuarioScfv usuario, BindingResult result){
        if(result.hasErrors()){
            if(usuario.getId() != null){
                return "usuario_scfv/edit";
            }
            return "usuario_scfv/create";
        }
        usuarioScfvService.saveUsuario(usuario);
        return "redirect:/usuarios/buscar";
    }

    @GetMapping("usuarios/detalhes/{id}")
    public String getUsuario(@PathVariable Integer id, Model model){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("registros", oficinaUsuarioService.getByUsuario(usuario));
        return "usuario_scfv/detalhes";
    }

    @Transactional
    @GetMapping("usuarios/delete/{id}")
    public String deletarUsuario(@PathVariable Integer id){
        usuarioScfvService.deleteUsuarioById(id);
        return "redirect:/usuarios/buscar";
    }

    @GetMapping("usuarios/edit/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(id);
        model.addAttribute("usuario", usuario);
        return "usuario_scfv/edit";
    }
}
