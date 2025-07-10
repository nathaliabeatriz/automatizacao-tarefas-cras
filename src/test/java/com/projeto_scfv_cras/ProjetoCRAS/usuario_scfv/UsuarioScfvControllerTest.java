package com.projeto_scfv_cras.ProjetoCRAS.usuario_scfv;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.time.LocalDate;
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
import com.projeto_scfv_cras.ProjetoCRAS.controller.UsuarioScfvController;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

@WebMvcTest(UsuarioScfvController.class)
@Import(TestConfig.class)
public class UsuarioScfvControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioScfvService usuarioScfvService;

    @AfterEach
    void resetMocks(){
        reset(usuarioScfvService);
    }

    private List<UsuarioScfv> createUsuarioList(){
        UsuarioScfv usuarioB = new UsuarioScfv();
        usuarioB.setId(1);
        usuarioB.setNome("Usuário B");
        usuarioB.setNis("111.111.111-11");
        usuarioB.setDataNascimento(LocalDate.of(2016, 6, 30));
        usuarioB.setNomeResponsavel("Responsável");
        usuarioB.setPrioridade(false);
        usuarioB.setBolsaFamilia(true);
        usuarioB.setTelefone("(12)3456-7890");

        return List.of(usuarioB);
    }

    private UsuarioScfv createUsuario(){
        UsuarioScfv usuario = new UsuarioScfv();
        usuario.setId(1);
        usuario.setNome("Usuário A");
        usuario.setNis("111.111.111-11");
        usuario.setDataNascimento(LocalDate.of(2016, 6, 30));
        usuario.setNomeResponsavel("Responsável");
        usuario.setPrioridade(false);
        usuario.setBolsaFamilia(true);
        usuario.setTelefone("(12)3456-7890");

        return usuario;
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /usuarios/save - Cadastrar novo usuário do SCFV válido")
    void testSaveValidUsuario() throws Exception{
        UsuarioScfv usuario = createUsuario();
        
        mockMvc.perform(post("/usuarios/save")
                        .with(csrf())
                        .flashAttr("usuario", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/usuarios/buscar"));

        verify(usuarioScfvService).saveUsuario(any(UsuarioScfv.class));
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /usuarios/save - Falha na validação e retorna para o formulário")
    void testSaveInvalidUsuario() throws Exception{
        UsuarioScfv usuario = new UsuarioScfv();
        mockMvc.perform(post("/usuarios/save")
                        .with(csrf())
                        .flashAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario_scfv/create"))
                .andExpect(model().attributeHasErrors("usuario"));;

        verify(usuarioScfvService, never()).saveUsuario(any(UsuarioScfv.class));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /usuarios/edit/1 - Acesso a rota de delete de usuário SCFV por usuário não autorizado")
    void testDeleteUsuarioNotAuthorizedUser() throws Exception{
        doNothing().when(usuarioScfvService).deleteUsuarioById(1);

        mockMvc.perform(get("/usuarios/delete/{id}", 1))
               .andExpect(status().isForbidden());
        
        verify(usuarioScfvService, never()).deleteUsuarioById(1);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /usuarios/buscar - Listar usuários SCFV cadastrados com usuário autenticado")
    void testShowListOfUsuarios() throws Exception{
        when(usuarioScfvService.getAllUsuarios()).thenReturn(createUsuarioList());
        mockMvc.perform(get("/usuarios/buscar"))
               .andExpect(status().isOk())
               .andExpect(view().name("usuario_scfv/buscar"))
               .andExpect(model().attributeExists("usuarios"))
               .andExpect(content().string(containsString("Buscar Usuário")))
               .andExpect(content().string(containsString("Usuário B")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /usuarios/delete/{id} - Deletar usuário SCFV com usuário autenticado")
    void testDeleteUsuario() throws Exception{
        doNothing().when(usuarioScfvService).deleteUsuarioById(1);

        mockMvc.perform(get("/usuarios/delete/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/usuarios/buscar"));

        verify(usuarioScfvService, times(1)).deleteUsuarioById(1);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /usuarios/detalhes/{id} - Verificar botões de editar e excluir para usuários não autorizados")
    void testShowEditAndDeleteLinkNotAuthorizedUser() throws Exception{
        UsuarioScfv usuario = createUsuario();
        usuario.setId(1);

        when(usuarioScfvService.getUsuarioById(1)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/detalhes/{id}", usuario.getId()))
               .andExpect(status().isOk())
               .andExpect(view().name("usuario_scfv/detalhes"))

               .andExpect(content().string(not(containsString("Editar Usuário"))))
               .andExpect(content().string(not(containsString("Deletar Usuário"))))
               .andExpect(content().string(not(containsString("Registrar em Oficina"))));

        verify(usuarioScfvService).getUsuarioById(1);
    }
}