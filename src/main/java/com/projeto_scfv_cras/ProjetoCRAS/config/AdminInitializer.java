package com.projeto_scfv_cras.ProjetoCRAS.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.projeto_scfv_cras.ProjetoCRAS.model.User;
import com.projeto_scfv_cras.ProjetoCRAS.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public AdminInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AdminProperties adminProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findUserByEmail(adminProperties.getEmail()).isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminProperties.getEmail());
            admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
            admin.setStatus("ATIVO");
            List<String> roles = new ArrayList<>();
            roles.add("Admin");
            admin.setRoles(roles);
            
            userRepository.save(admin);
        }
    }
}
