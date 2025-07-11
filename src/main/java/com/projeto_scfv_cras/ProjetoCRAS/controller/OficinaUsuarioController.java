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
    public String telaListarOficinasRegistro(@PathVariable Integer id, @RequestParam(required = false) String nome, @RequestParam(required = false) String msg, Model model){
        if(nome == null){
            nome = "";
        }

        List<Oficina> oficinas = oficinaService.getOficinasNaoRegistradasAoUsuario(id, nome);
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(id);

        if("erro".equals(msg)){
            model.addAttribute("msgErro", "Erro no registro! Não há mais vagas disponíveis para essa oficina");
        }
        else if("ok".equals(msg)){
            model.addAttribute("msgOk", "Usuário registrado a oficina com sucesso!");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("oficinas", oficinas);
        return "oficina_usuario/listOficinas";
    }

    @GetMapping("registros/listUsuarios/{id}")
    public String telaListarUsuariosRegistro(@PathVariable Integer id, @RequestParam(required = false) String nome, @RequestParam(required = false) String msg, Model model){
        if(nome == null){
            nome = "";
        }

        List<UsuarioScfv> usuarios = usuarioScfvService.getUsuariosNaoRegistradosAOficina(id, nome);
        Oficina oficina = oficinaService.getOficinaById(id);

        if("erro".equals(msg)){
            model.addAttribute("msgErro", "Erro no registro! Não há mais vagas disponíveis para essa oficina");
        }
        else if("ok".equals(msg)){
            model.addAttribute("msgOk", "Usuário registrado a oficina com sucesso!");
        }

        model.addAttribute("oficina", oficina);
        model.addAttribute("usuarios", usuarios);
        return "oficina_usuario/listUsuarios";
    }

    @GetMapping("registros/registrarUsuario/{idOficina}/{idUsuario}/{flag}")
    public String registrar(@PathVariable Integer idOficina, @PathVariable Integer idUsuario, @PathVariable String flag){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(idUsuario);
        Oficina oficina = oficinaService.getOficinaById(idOficina);

        String msg;
        if(oficina.getVagasOcupadas() < oficina.getQtdVagas()){
            oficinaUsuarioService.registrarUsuarioEmOficina(oficina, usuario);
            oficina.setVagasOcupadas(oficina.getVagasOcupadas() + 1);
            oficinaService.saveOficina(oficina);
            msg = "ok";

        } else msg = "erro";

        if(flag.equals("oficina")){
            return "redirect:/registros/listOficinas/" + idUsuario + "?msg=" + msg;
        }
        return "redirect:/registros/listUsuarios/" + idOficina + "?msg=" + msg;
    }

    @GetMapping("registros/deletarRegistro/{idOficina}/{idUsuario}/{flag}")
    public String deletarRegistro(@PathVariable Integer idOficina, @PathVariable Integer idUsuario, @PathVariable String flag){
        UsuarioScfv usuario = usuarioScfvService.getUsuarioById(idUsuario);
        Oficina oficina = oficinaService.getOficinaById(idOficina);
        
        oficinaUsuarioService.deleteByUsuarioAndOficina(oficina, usuario);
        oficina.setVagasOcupadas(oficina.getVagasOcupadas() - 1);
        oficinaService.saveOficina(oficina);

        if(flag.equals("usuario")){
            return "redirect:/usuarios/detalhes/" + idUsuario;
        }
        return "redirect:/oficinas/detalhes/" + idOficina;
    }
}
