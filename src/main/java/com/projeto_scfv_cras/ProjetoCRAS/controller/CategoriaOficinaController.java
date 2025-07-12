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

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.service.CategoriaOficinaService;

import jakarta.validation.Valid;

@Controller
public class CategoriaOficinaController {
    @Autowired
    private CategoriaOficinaService categoriaOficinaService;

    @GetMapping("categorias")
    public String listarCategorias(Model model){
        List<CategoriaOficina> categorias = categoriaOficinaService.getAllCategorias();
        model.addAttribute("categorias", categorias);
        return "categoria_oficina/index";
    }

    @GetMapping("categorias/create")
    public String create(Model model){
        model.addAttribute("categoria", new CategoriaOficina());
        return "categoria_oficina/create";
    }

    @PostMapping("categorias/save")
    public String salvarCategoria(@ModelAttribute("categoria") @Valid CategoriaOficina categoria, BindingResult result){
        if(result.hasErrors()){
            if(categoria.getId() != null){
                return "categoria_oficina/edit";
            }
            return "categoria_oficina/create";
        }
        categoriaOficinaService.saveCategoria(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("categorias/delete/{id}")
    public String deletarCategoria(@PathVariable Integer id){
        categoriaOficinaService.deleteCategoriaById(id);
        return "redirect:/categorias";
    }

    @GetMapping("categorias/edit/{id}")
    public String editarOficina(@PathVariable Integer id, Model model){
        CategoriaOficina categoria = categoriaOficinaService.getCategoriaById(id);
        model.addAttribute("categoria", categoria);
        return "categoria_oficina/edit";
    }
}
