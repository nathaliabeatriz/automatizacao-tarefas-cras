package com.projeto_scfv_cras.ProjetoCRAS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OficinaController {
    @GetMapping("oficinas")
    public String menuOficinas(){
        return "/oficina/index";
    }
}
