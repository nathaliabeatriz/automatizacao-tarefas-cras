package com.projeto_scfv_cras.ProjetoCRAS.categoria_oficina;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.projeto_scfv_cras.ProjetoCRAS.controller.CategoriaOficinaController;
import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.service.CategoriaOficinaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(CategoriaOficinaController.class)
@Import(TestConfig.class)
public class CategoriaOficinaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaOficinaService categoriaOficinaService;

    @AfterEach
    void resetMocks(){
        reset(categoriaOficinaService);
    }

    private List<CategoriaOficina> createListCategoria(){
        CategoriaOficina categoriaOficina = createCategoria();
        return List.of(categoriaOficina);
    }

    private CategoriaOficina createCategoria(){
        CategoriaOficina categoriaOficina = new CategoriaOficina();
        categoriaOficina.setId(1);
        categoriaOficina.setNome("Categoria A");
        categoriaOficina.setDescricao("Descrição");
        return categoriaOficina;
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /categorias/save - Cadastrar nova categoria válida")
    void testSaveValidCategoria() throws Exception{
        CategoriaOficina categoria = createCategoria();
        
        mockMvc.perform(post("/categorias/save")
                        .with(csrf())
                        .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categorias"));

        verify(categoriaOficinaService).saveCategoria(any(CategoriaOficina.class));
    }

    @Test
    @WithMockUser(username = "funcionario@gmail.com", authorities = { "Funcionario" })
    @DisplayName("POST /categorias/save - Falha na validação e retorna para o formulário")
    void testSaveInvalidCategoria() throws Exception{
        CategoriaOficina categoria = new CategoriaOficina();
        mockMvc.perform(post("/categorias/save")
                        .with(csrf())
                        .flashAttr("categoria", categoria))
                .andExpect(status().isOk())
                .andExpect(view().name("categoria_oficina/create"))
                .andExpect(model().attributeHasErrors("categoria"));;

        verify(categoriaOficinaService, never()).saveCategoria(any(CategoriaOficina.class));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Tecnico" })
    @DisplayName("GET /categorias/create - Acesso a formulário de cadastro de categoria por usuário não autorizado")
    void testShowCreateFormCategoriaNotAuthorizedUser() throws Exception{
        mockMvc.perform(get("/categorias/create"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /categorias/buscar - Listar categorias cadastradas com usuário autenticado")
    void testShowListOfCategorias() throws Exception{
        when(categoriaOficinaService.getAllCategorias()).thenReturn(createListCategoria());
        mockMvc.perform(get("/categorias"))
               .andExpect(status().isOk())
               .andExpect(view().name("categoria_oficina/index"))
               .andExpect(model().attributeExists("categorias"))
               .andExpect(content().string(containsString("Listar Categorias de Oficina")))
               .andExpect(content().string(containsString("Categoria A")));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", authorities = { "Funcionario" })
    @DisplayName("GET /categorias/delete/{id} - Deletar categoria com usuário autenticado")
    void testDeleteCategoria() throws Exception{
        doNothing().when(categoriaOficinaService).deleteCategoriaById(1);

        mockMvc.perform(get("/categorias/delete/{id}", 1))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/categorias"));

        verify(categoriaOficinaService, times(1)).deleteCategoriaById(1);
    }
}
