package com.projeto_scfv_cras.ProjetoCRAS.config;

import java.util.List;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.projeto_scfv_cras.ProjetoCRAS.service.CategoriaOficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaService;
import com.projeto_scfv_cras.ProjetoCRAS.service.OficinaUsuarioService;
import com.projeto_scfv_cras.ProjetoCRAS.service.UserService;
import com.projeto_scfv_cras.ProjetoCRAS.service.UsuarioScfvService;

@Import(SecurityConfig.class)
@TestConfiguration
public class TestConfig {
    @Bean
    public UserService userService(){
        return Mockito.mock(UserService.class);
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> new User(username, "password", List.of(new SimpleGrantedAuthority("")));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OficinaService oficinaService(){
        return Mockito.mock(OficinaService.class);
    }

    @Bean
    public UsuarioScfvService usuarioScfvService(){
        return Mockito.mock(UsuarioScfvService.class);
    }

    @Bean
    public OficinaUsuarioService oficinaUsuarioService(){
        return Mockito.mock(OficinaUsuarioService.class);
    }

    @Bean
    public CategoriaOficinaService categoriaOficinaService(){
        return Mockito.mock(CategoriaOficinaService.class);
    }
}
