package com.projeto_scfv_cras.ProjetoCRAS.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto_scfv_cras.ProjetoCRAS.model.User;
import com.projeto_scfv_cras.ProjetoCRAS.repository.UserRepository;
import com.projeto_scfv_cras.ProjetoCRAS.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Integer saveUser(User user) {
        String passwd = user.getPassword();
        String encodedPasswod = passwordEncoder.encode(passwd);
        user.setPassword(encodedPasswod);
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<User> opt = userRepository.findUserByEmail(email);
        User user = null;

        if(opt.isEmpty()){
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        else{
            user = opt.get();
            if(!user.getStatus().equals("ATIVO")){
                throw new DisabledException("Usuário ainda não foi aprovado");
            }
        }
        return user;
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).get();
    }

    public void updateUser(User user){
        userRepository.save(user);
    }
}
