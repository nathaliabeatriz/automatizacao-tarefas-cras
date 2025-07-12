package com.projeto_scfv_cras.ProjetoCRAS.oficina_usuario;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.containsString;
import com.projeto_scfv_cras.ProjetoCRAS.config.TestConfig;
import com.projeto_scfv_cras.ProjetoCRAS.controller.OficinaUsuarioController;
import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

@WebMvcTest(OficinaUsuarioController.class)
@Import(TestConfig.class)
public class OficinaUsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OficinaUsuarioService oficinaUsuarioService;

    @Autowired
    private UsuarioScfvService usuarioScfvService;

    @Autowired
    private OficinaService oficinaService;

    @AfterEach
    void resetMocks(){
        reset(oficinaUsuarioService);
        reset(usuarioScfvService);
        reset(oficinaService);
    }

    private List<UsuarioScfv> createListUsuarios(){
        UsuarioScfv usuarioScfv = createUsuario();
        return List.of(usuarioScfv);
    }

    private List<Oficina> createListOficinas(){
        Oficina oficina = createOficina();
        return List.of(oficina);
    }

    private Oficina createOficina(){
        Oficina oficina = new Oficina();
        CategoriaOficina categoria = new CategoriaOficina();
        categoria.setNome("Categoria A");
        oficina.setId(1);
        oficina.setNome("Oficina A");
        oficina.setQtdVagas(10);
        oficina.setHorarioInicio(LocalTime.of(10, 0));
        oficina.setHorarioTermino(LocalTime.of(11, 0));
        oficina.setDiaSemana("Segunda");
        oficina.setCategoria(categoria);
        oficina.setVagasOcupadas(0);

        return oficina;
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
    @DisplayName("POST /registros/registrarUsuario/{idOficina}/{idUsuario}/{flag} - Registrar usuário em oficina")
    void testRegistrarUsuarioEmOficina() throws Exception{
        Oficina oficina = createOficina();
        UsuarioScfv usuarioScfv = createUsuario();

        mockMvc.perform(get("/registros/registrarUsuario/{idOficina}/{idUsuario}/{flag}", oficina.getId(), usuarioScfv.getId(), "oficina"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registros/listOficinas/" + usuarioScfv.getId() + "?msg=erro"));

        verify(oficinaUsuarioService).registrarUsuarioEmOficina(null, null);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /registos/deletarRegistro/{idOficina}/{idUsuario}/{flag} - Deletar registro de usuário em oficina com usuário autenticado")
    void testDeleteRegistro() throws Exception{
        Oficina oficina = createOficina();
        UsuarioScfv usuarioScfv = createUsuario();

        mockMvc.perform(get("/registros/deletarRegistro/{idOficina}/{idUsuario}/{flag}", oficina.getId(), usuarioScfv.getId(), "oficina"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/oficinas/detalhes/" + oficina.getId()));

        verify(oficinaUsuarioService).deleteByUsuarioAndOficina(null, null);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /registros/listUsuarios/{id} - Listar usuários não registrados em oficina")
    void testShowListOfUsuariosNaoRegistradosEmOficina() throws Exception{
        when(oficinaService.getOficinaById(1)).thenReturn(createOficina());
        when(usuarioScfvService.getUsuariosNaoRegistradosAOficina(1, "")).thenReturn(createListUsuarios());
        mockMvc.perform(get("/registros/listUsuarios/{id}", 1))
               .andExpect(status().isOk())
               .andExpect(view().name("oficina_usuario/listUsuarios"))
               .andExpect(model().attributeExists("oficina"))
               .andExpect(model().attributeExists("usuarios"))
               .andExpect(content().string(containsString("Usuário A")))
               .andExpect(content().string(containsString("Oficina A")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /registros/listOficinas/{id} - Listar oficinas não associadas ao usuário")
    void testShowListOfOficinasNaoAssociadasAoUsuario() throws Exception{
        when(usuarioScfvService.getUsuarioById(1)).thenReturn(createUsuario());
        when(oficinaService.getOficinasNaoRegistradasAoUsuario(1, "")).thenReturn(createListOficinas());
        mockMvc.perform(get("/registros/listOficinas/{id}", 1))
               .andExpect(status().isOk())
               .andExpect(view().name("oficina_usuario/listOficinas"))
               .andExpect(model().attributeExists("oficinas"))
               .andExpect(model().attributeExists("usuario"))
               .andExpect(content().string(containsString("Usuário A")))
               .andExpect(content().string(containsString("Oficina A")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /registros/listOficinas/{id} - Listar oficinas não associadas ao usuário com usuário não autorizado")
    void testShowListOfOficinasNaoAssociadasAoUsuarioNotAuthorizedUser() throws Exception{
        when(usuarioScfvService.getUsuarioById(1)).thenReturn(createUsuario());
        when(oficinaService.getOficinasNaoRegistradasAoUsuario(1, "")).thenReturn(createListOficinas());
        mockMvc.perform(get("/registros/listOficinas/{id}", 1))
               .andExpect(status().isForbidden());
    }
}
