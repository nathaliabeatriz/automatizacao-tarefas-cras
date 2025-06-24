package com.projeto_scfv_cras.ProjetoCRAS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;

import jakarta.validation.Valid;

@Controller
public class OficinaController {
    @Autowired
    private OficinaService oficinaService;

    @GetMapping("oficinas")
    public String menuOficinas(){
        return "oficina/index";
    }

    @GetMapping("oficinas/create")
    public String create(Model model){
        model.addAttribute("oficina", new Oficina());
        return "oficina/create";
    }

    @PostMapping("oficinas/save")
    public String salvarOficina(@ModelAttribute @Valid Oficina oficina, BindingResult result){
        if(result.hasErrors()){
            return "oficina/create";
        }
        oficinaService.saveOficina(oficina);
        return "redirect:/oficinas";
    }
}
