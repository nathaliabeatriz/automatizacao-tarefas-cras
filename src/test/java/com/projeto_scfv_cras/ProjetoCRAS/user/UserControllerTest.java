package com.projeto_scfv_cras.ProjetoCRAS.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.projeto_scfv_cras.ProjetoCRAS.config.TestConfig;
import com.projeto_scfv_cras.ProjetoCRAS.controller.UserController;
import com.projeto_scfv_cras.ProjetoCRAS.model.User;
import com.projeto_scfv_cras.ProjetoCRAS.service.UserService;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @AfterEach
    void resetMocks(){
        reset(userService);
    }

    private List<User> createUserList(){
        User userB = new User();
        userB.setId(1);
        userB.setName("User B");
        userB.setEmail("userb@gmail.com");
        userB.setRoles(List.of("Funcionario"));
        userB.setStatus("PENDENTE");
    
        return List.of(userB);
    }

    @Test
    @DisplayName("GET /user - Tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("POST /saveUser - Registrar novo usuário válido")
    void testSaveValidUser() throws Exception{
        User user = new User();
        user.setName("Usuario");
        user.setEmail("usuario@gmail.com");
        user.setPassword("123TestPassword");
        user.setStatus("PENDENTE");
        user.setRoles(List.of("Tecnico"));

        mockMvc.perform(post("/saveUser")
                        .with(csrf())
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"))
                .andExpect(content().string(containsString("<div class=\"alert alert-success\">Seu usuário foi registrado com sucesso! Aguarde até que o administrador aprove seu cadastro! Contato: admin@gmail.com</div>")));

        verify(userService).saveUser(any(User.class));
    }

    @Test
    @DisplayName("POST /saveUser - Falha na validação e retorna para o formulário")
    void testSaveInvalidUser() throws Exception{
        User user = new User();
        mockMvc.perform(post("/saveUser")
                        .with(csrf())
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerUser"));

        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", authorities = { "Admin" })
    @DisplayName("GET /user/listarUsuarios - Listar usuários para verificação | Usuário autorizado")
    void testShowListOfUsersAuthorizedUser() throws Exception{
        when(userService.getAllUsers()).thenReturn(createUserList());
        mockMvc.perform(get("/user/listarUsuarios"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/listarUsuarios"))
               .andExpect(model().attributeExists("usuarios"))
               .andExpect(content().string(containsString("Validar Usuários")))
               .andExpect(content().string(containsString("User B")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /user/listarUsuarios - Listar usuários para verificação | Usuário não autorizado")
    void testShowListOfUsersNotAuthorizedUser() throws Exception{
        when(userService.getAllUsers()).thenReturn(createUserList());
        mockMvc.perform(get("/user/listarUsuarios"))
               .andExpect(status().isForbidden());
    }
}


