package com.projeto_scfv_cras.ProjetoCRAS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_scfv_cras.ProjetoCRAS.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Integer>{
    Optional<User> findUserByEmail(String email);
}
