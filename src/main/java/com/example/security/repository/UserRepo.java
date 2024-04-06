package com.example.security.repository;



import com.example.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepo extends JpaRepository<User, Integer> {

    public User findByEmail(String emaill);

}
