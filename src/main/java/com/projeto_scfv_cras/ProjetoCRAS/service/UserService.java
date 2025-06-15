package com.projeto_scfv_cras.ProjetoCRAS.service;

import java.util.List;

import com.projeto_scfv_cras.ProjetoCRAS.model.User;

public interface UserService {
    public Integer saveUser(User user);
    List<User> getAllUsers();
    public User findUserByEmail(String email);
    public void updateUser(User user);
}
