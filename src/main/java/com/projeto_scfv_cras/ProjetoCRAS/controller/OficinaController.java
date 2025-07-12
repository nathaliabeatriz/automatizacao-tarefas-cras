package com.projeto_scfv_cras.ProjetoCRAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.service.CategoriaOficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;

import jakarta.validation.Valid;

@Controller
public class OficinaController {
    @Autowired
    private OficinaService oficinaService;
    @Autowired
    private OficinaUsuarioService oficinaUsuarioService;
    @Autowired
    private CategoriaOficinaService categoriaOficinaService;

    @GetMapping("oficinas")
    public String menuOficinas(){
        return "oficina/index";
    }

    @GetMapping("oficinas/create")
    public String create(Model model){
        List<CategoriaOficina> categorias = categoriaOficinaService.getAllCategorias();
        model.addAttribute("oficina", new Oficina());
        model.addAttribute("categorias", categorias);
        return "oficina/create";
    }

    @GetMapping("oficinas/buscar")
    public String buscarOficinas(@RequestParam(required = false) String nome, Model model){
        List<Oficina> oficinas;
        if(nome != null && !nome.isBlank()){
            oficinas = oficinaService.getOficinaByNome(nome);
        } else{
            oficinas = oficinaService.getAllOficinas();
        }
        model.addAttribute("oficinas", oficinas);
        model.addAttribute("qtdResultados", oficinas.size());
        return "oficina/buscar";
    }

    @GetMapping("oficinas/detalhes/{id}")
    public String getOficina(@PathVariable Integer id, Model model){
        Oficina oficina = oficinaService.getOficinaById(id);
        model.addAttribute("oficina", oficina);
        model.addAttribute("registros", oficinaUsuarioService.getByOficina(oficina));
        return "oficina/detalhes";
    }

    @GetMapping("oficinas/edit/{id}")
    public String editarOficina(@PathVariable Integer id, Model model){
        Oficina oficina = oficinaService.getOficinaById(id);
        List<CategoriaOficina> categorias = categoriaOficinaService.getAllCategorias();
        model.addAttribute("oficina", oficina);
        model.addAttribute("categorias", categorias);
        return "oficina/edit";
    }

    @PostMapping("oficinas/save")
    public String salvarOficina(@ModelAttribute @Valid Oficina oficina, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("categorias", categoriaOficinaService.getAllCategorias());
            if(oficina.getId() != null){
                return "oficina/edit";
            }
            return "oficina/create";
        }
        oficinaService.saveOficina(oficina);
        return "redirect:/oficinas/buscar";
    }

    @GetMapping("oficinas/delete/{id}")
    public String deletarOficina(@PathVariable Integer id){
        oficinaService.deleteOficinaById(id);
        return "redirect:/oficinas/buscar";
    }
}
