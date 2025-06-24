package com.projeto_scfv_cras.ProjetoCRAS.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User implements UserDetails{
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;
    
    @NotBlank(message = "Nome é um campo obrigatório")
    @Size(min = 3, max = 100, message = "Nome do usuário deve conter entre 3 e 100 caracteres")
    @Column(name = "user_name")
    private String name;

    @NotBlank(message = "Email é um campo obrigatório")
    @Email(message = "Digite um e-mail válido")
    @Column(name = "user_email")
    private String email;

    @NotBlank(message = "Senha é um campo obrigatório")
    @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número")
    @Column(name = "user_password")
    private String password;

    @Column(name = "user_status")
    private String status;

    @Size(min = 1, message = "Escolha pelo menos um papel")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role")
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> ga = new HashSet<>();
            for(String role : roles){
                ga.add(new SimpleGrantedAuthority(role));
        }
        return ga;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
