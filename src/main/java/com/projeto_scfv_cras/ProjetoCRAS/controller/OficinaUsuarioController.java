package com.projeto_scfv_cras.ProjetoCRAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

@Controller
public class OficinaUsuarioController {
    @Autowired
    private OficinaUsuarioService oficinaUsuarioService;

    @Autowired
    private UsuarioScfvService usuarioScfvService;

    @Autowired
    private OficinaService oficinaService;

    @GetMapping("registros/listOficinas/{id}")
    public String telaListarOficinasRegistro(@PathVariable Integer id, @RequestParam(required = false) String nome, Model model){
        if(nome == null){
            nome = "";
        }

        List<Oficina> oficinas = oficinaService.getOficinasNaoRegistradasAoUsuario(id, nome);
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("oficinas", oficinas);
        return "oficina_usuario/listOficinas";
    }

    @GetMapping("registros/listUsuarios/{id}")
    public String telaListarUsuariosRegistro(@PathVariable Integer id, @RequestParam(required = false) String nome, Model model){
        if(nome == null){
            nome = "";
        }

        List<UsuarioScfv> usuarios = usuarioScfvService.getUsuariosNaoRegistradosAOficina(id, nome);
        Oficina oficina = oficinaService.getOficinaById(id);

        model.addAttribute("oficina", oficina);
        model.addAttribute("usuarios", usuarios);
        return "oficina_usuario/listUsuarios";
    }

    @GetMapping("registros/registrarUsuario/{idOficina}/{idUsuario}/{flag}")
    public String registrar(@PathVariable Integer idOficina, @PathVariable Integer idUsuario, @PathVariable String flag){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(idUsuario);
        Oficina oficina = oficinaService.getOficinaById(idOficina);

        oficinaUsuarioService.registrarUsuarioEmOficina(oficina, usuario);
        if(flag.equals("oficina")){
            return "redirect:/registros/listOficinas/" + idUsuario;
        }
        return "redirect:/registros/listUsuarios/" + idOficina;
    }

    @GetMapping("registros/deletarRegistro/{idOficina}/{idUsuario}/{flag}")
    public String deletarRegistro(@PathVariable Integer idOficina, @PathVariable Integer idUsuario, @PathVariable String flag){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(idUsuario);
        Oficina oficina = oficinaService.getOficinaById(idOficina);
        
        oficinaUsuarioService.deleteByUsuarioAndOficina(oficina, usuario);
        if(flag.equals("usuario")){
            return "redirect:/usuarios/detalhes/" + idUsuario;
        }
        return "redirect:/oficinas/detalhes/" + idOficina;
    }
}
