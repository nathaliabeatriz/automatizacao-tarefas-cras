package com.projeto_scfv_cras.ProjetoCRAS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import com.projeto_scfv_cras.ProjetoCRAS.model.CategoriaOficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.Oficina;
import com.projeto_scfv_cras.ProjetoCRAS.model.UsuarioScfv;
import com.projeto_scfv_cras.ProjetoCRAS.repository.CategoriaOficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.OficinaUsuarioRepository;
import com.projeto_scfv_cras.ProjetoCRAS.repository.UsuarioScfvRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
@Transactional
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioScfvRepository usuarioScfvRepository;

    @Autowired
    private OficinaRepository oficinaRepository;

    @Autowired
    private OficinaUsuarioRepository oficinaUsuarioRepository;

    @Autowired
    private CategoriaOficinaRepository categoriaOficinaRepository;

    private UsuarioScfv createUsuario(){
        UsuarioScfv usuario = new UsuarioScfv();
        usuario.setNome("Usuário A");
        usuario.setNis("111.111.111-11");
        usuario.setDataNascimento(LocalDate.of(2016, 6, 30));
        usuario.setNomeResponsavel("Responsável");
        usuario.setPrioridade(false);
        usuario.setBolsaFamilia(true);
        usuario.setTelefone("(12)3456-7890");

        return usuario;
    }

    private Oficina createOficina(){
        Oficina oficina = new Oficina();
        CategoriaOficina categoria = createCategoria();
        oficina.setNome("Oficina A");
        oficina.setQtdVagas(10);
        oficina.setHorarioInicio(LocalTime.of(10, 0));
        oficina.setHorarioTermino(LocalTime.of(11, 0));
        oficina.setDiaSemana("Segunda");
        oficina.setVagasOcupadas(0);
        oficina.setCategoria(categoria);

        return oficina;
    }

    private CategoriaOficina createCategoria(){
        CategoriaOficina categoria = new CategoriaOficina();
        categoria.setNome("Categoria A");
        categoria.setDescricao("Descrição");

        return categoria;
    }
    
    @Test
    @WithMockUser(authorities = { "Funcionario" })
    void testSaveUsuarioIntegration() throws Exception{
        UsuarioScfv usuarioScfv = createUsuario();
        mockMvc.perform(post("/usuarios/save")
                .with(csrf())
                .flashAttr("usuario", usuarioScfv))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/buscar"));

        assertTrue(usuarioScfvRepository.findAll()
                .stream()
                .anyMatch(u -> "Usuário A".equals(u.getNome())));
    }

    @Test
    @WithMockUser(authorities = { "Funcionario" })
    void testSaveOficinaIntegration() throws Exception{
        Oficina oficina = createOficina();
        CategoriaOficina categoria = createCategoria();
        mockMvc.perform(post("/categorias/save")
                .with(csrf())
                .flashAttr("categoria", categoria));

        CategoriaOficina categoriaSalva = categoriaOficinaRepository
            .findByNome("Categoria A");
        
        oficina.setCategoria(categoriaSalva);

        mockMvc.perform(post("/oficinas/save")
                .with(csrf())
                .flashAttr("oficina", oficina))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oficinas/buscar"));

        assertTrue(oficinaRepository.findAll()
                .stream()
                .anyMatch(o -> "Oficina A".equals(o.getNome())));
    }

    @Test
    @WithMockUser(authorities = { "Funcionario" })
    void testSaveRegistroOficinaUsuarioIntegration() throws Exception{
        Oficina oficina = createOficina();
        UsuarioScfv usuarioScfv = createUsuario();
        CategoriaOficina categoria = createCategoria();
        mockMvc.perform(post("/categorias/save")
                .with(csrf())
                .flashAttr("categoria", categoria));

        CategoriaOficina categoriaSalva = categoriaOficinaRepository
            .findByNome("Categoria A");
        
        oficina.setCategoria(categoriaSalva);

        mockMvc.perform(post("/oficinas/save")
                .with(csrf())
                .flashAttr("oficina", oficina));
        
        mockMvc.perform(post("/usuarios/save")
                .with(csrf())
                .flashAttr("usuario", usuarioScfv));
                
        mockMvc.perform(get("/registros/registrarUsuario/{idOficina}/{idUsuario}/{flag}", oficina.getId(), usuarioScfv.getId(), "oficina"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registros/listOficinas/" + usuarioScfv.getId() + "?msg=ok"));

        assertTrue(oficinaUsuarioRepository.findAll()
                .stream()
                .anyMatch(o -> "Oficina A".equals(o.getOficina().getNome()) && "Usuário A".equals(o.getUsuario().getNome())));
    }

    @Test
    @WithMockUser(authorities = { "Funcionario" })
    void testSaveCategoriaIntegration() throws Exception{
        CategoriaOficina categoriaOficina = createCategoria();
        mockMvc.perform(post("/categorias/save")
                .with(csrf())
                .flashAttr("categoria", categoriaOficina))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categorias"));

        assertTrue(categoriaOficinaRepository.findAll()
                .stream()
                .anyMatch(c -> "Categoria A".equals(c.getNome())));
    }
}
