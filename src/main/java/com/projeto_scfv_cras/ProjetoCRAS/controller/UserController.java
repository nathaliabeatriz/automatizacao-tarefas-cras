package com.projeto_scfv_cras.ProjetoCRAS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto_scfv_cras.ProjetoCRAS.model.User;
import com.projeto_scfv_cras.ProjetoCRAS.service.UserService;
import org.springframework.security.core.Authentication;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "user/registerUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        String message = "Seu usuário foi registrado com sucesso! Aguarde até que o administrador aprove seu cadastro! Contato: admin@gmail.com";
        model.addAttribute("msg", message);
        return "/login";
    }

    @GetMapping("/login")
    public String login(Authentication authenticaton){
        if(authenticaton != null && authenticaton.isAuthenticated()){
            return "redirect:home";
        }
        return "/login";
    }

    @GetMapping("user/listarUsuarios")
    public String listarUsuarios(Model model){
        List<User> usuarios = userService.getAllUsers();
        model.addAttribute("usuarios", usuarios);
        return "user/listarUsuarios";
    }

    @GetMapping("/accessDenied")
	public String getAccessDeniedPage() {
		return "user/accessDenied";
	}

    @PostMapping("/user/alterarStatus")
    public String atualizarStatus(@RequestParam String email, @RequestParam String status) {
        User user = userService.findUserByEmail(email);
        user.setStatus(status);
        userService.updateUser(user);
        return "redirect:listarUsuarios";
    }
    
}