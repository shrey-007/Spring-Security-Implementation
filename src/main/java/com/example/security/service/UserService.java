package com.example.security.service;


import com.example.security.entity.User;

public interface UserService {

    public User saveUser(User user);

    public void removeSessionMessage();

}
