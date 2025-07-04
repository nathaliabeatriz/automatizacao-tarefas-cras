package com.projeto_scfv_cras.ProjetoCRAS.oficina;

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

import com.projeto_scfv_cras.ProjetoCRAS.config.TestConfig;
import com.projeto_scfv_cras.ProjetoCRAS.controller.OficinaController;
import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;

@WebMvcTest(OficinaController.class)
@Import(TestConfig.class)
public class OficinaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OficinaService oficinaService;

    @AfterEach
    void resetMocks(){
        reset(oficinaService);
    }

    private List<Oficina> createOficinaList(){
        Oficina oficinaB = new Oficina();
        oficinaB.setId(1);
        oficinaB.setNome("Oficina B");
        oficinaB.setQtdVagas(10);
        oficinaB.setHorarioInicio(LocalTime.of(10, 0));
        oficinaB.setHorarioTermino(LocalTime.of(11, 0));
        oficinaB.setDiaSemana("Segunda");

        return List.of(oficinaB);
    }

    private Oficina createOficina(){
        Oficina oficina = new Oficina();
        oficina.setNome("Oficina A");
        oficina.setQtdVagas(10);
        oficina.setHorarioInicio(LocalTime.of(10, 0));
        oficina.setHorarioTermino(LocalTime.of(11, 0));
        oficina.setDiaSemana("Segunda");

        return oficina;
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /oficinas/save - Cadastrar nova oficina válida")
    void testSaveValidOficina() throws Exception{
        Oficina oficina = createOficina();
        
        mockMvc.perform(post("/oficinas/save")
                        .with(csrf())
                        .flashAttr("oficina", oficina))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/oficinas/buscar"));

        verify(oficinaService).saveOficina(any(Oficina.class));
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /oficinas/save - Falha na validação e retorna para o formulário")
    void testSaveInvalidOficina() throws Exception{
        Oficina oficina = new Oficina();
        mockMvc.perform(post("/oficinas/save")
                        .with(csrf())
                        .flashAttr("oficina", oficina))
                .andExpect(status().isOk())
                .andExpect(view().name("oficina/create"))
                .andExpect(model().attributeHasErrors("oficina"));;

        verify(oficinaService, never()).saveOficina(any(Oficina.class));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /oficinas/create - Acesso a formulário de cadastro de oficina por usuário não autorizado")
    void testShowCreateFormOficinaNotAuthorizedUser() throws Exception{
        mockMvc.perform(get("/oficinas/create"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /oficinas/buscar - Listar oficinas cadastradas com usuário autenticado")
    void testShowListOfOficinas() throws Exception{
        when(oficinaService.getAllOficinas()).thenReturn(createOficinaList());
        mockMvc.perform(get("/oficinas/buscar"))
               .andExpect(status().isOk())
               .andExpect(view().name("oficina/buscar"))
               .andExpect(model().attributeExists("oficinas"))
               .andExpect(content().string(containsString("Buscar Oficina")))
               .andExpect(content().string(containsString("Oficina B")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /oficinas/delete/{id} - Deletar oficina com usuário autenticado")
    void testDeleteOficina() throws Exception{
        doNothing().when(oficinaService).deleteOficinaById(1);

        mockMvc.perform(get("/oficinas/delete/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/oficinas/buscar"));

        verify(oficinaService, times(1)).deleteOficinaById(1);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /oficinas/detalhes/{id} - Verificar botões de editar e excluir para usuários não autorizados")
    void testShowEditAndDeleteLinkNotAuthorizedUser() throws Exception{
        Oficina oficina = createOficina();
        oficina.setId(1);

        when(oficinaService.getOficinaById(1)).thenReturn(oficina);

        mockMvc.perform(get("/oficinas/detalhes/{id}", oficina.getId()))
               .andExpect(status().isOk())
               .andExpect(view().name("oficina/detalhes"))

               .andExpect(content().string(not(containsString("<a th:href=\"@{/oficinas/edit/{id} (id=${oficina.id})}\" class=\"btn btn-editar w-50\">Editar Oficina</a>\r\n" + //
                                      "                <a data-bs-toggle=\"modal\" data-bs-target=\"#confirmarExclusaoModal\" class=\"btn btn-deletar w-50\">Deletar Oficina</a>"))));

        verify(oficinaService).getOficinaById(1);
    }
}
