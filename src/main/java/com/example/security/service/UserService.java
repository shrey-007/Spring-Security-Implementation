package com.example.security.service;


import com.example.security.entity.User;

public interface UserService {

    public User saveUser(User user);

    public void removeSessionMessage();
    public void increaseFailedAttempt(User user);
    public void resetAttempt(String email);
    public void lock(User user);
    public boolean  unlockAccountTimeExpired(User user);

}
