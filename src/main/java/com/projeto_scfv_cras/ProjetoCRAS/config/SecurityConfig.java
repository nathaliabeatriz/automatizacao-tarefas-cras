package com.projeto_scfv_cras.ProjetoCRAS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService uds;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/home", "/register", "/saveUser", "/login").permitAll()
				.requestMatchers("user/listarUsuarios").hasAuthority("Admin")
				.requestMatchers("/", "/oficinas", "/oficinas/buscar", "/oficinas/detalhes/*").hasAnyAuthority("Admin", "Funcionario", "Tecnico")
				.requestMatchers("/oficinas/**").hasAnyAuthority("Admin", "Funcionario")
				.requestMatchers("/usuarios/editar/*", "/usuarios/delete/*").hasAnyAuthority("Admin", "Funcionario")
				.requestMatchers("/usuarios/**").hasAnyAuthority("Admin", "Funcionario", "Tecnico")
				.requestMatchers("/registros/**").hasAnyAuthority("Admin", "Funcionario")
				.requestMatchers("/categorias/**").hasAnyAuthority("Admin", "Funcionario")
                .anyRequest().authenticated())
                .formLogin(form -> form
					.loginPage("/login")
					.defaultSuccessUrl("/", true)
					.failureUrl("/login?error=true")
					.permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
    					.logoutSuccessUrl("/login?logout")
    					.invalidateHttpSession(true)
    					.deleteCookies("JSESSIONID")
    					.permitAll())
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/accessDenied"))
                .authenticationProvider(authenticationProvider());
		
		return http.build();
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(uds);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}
}