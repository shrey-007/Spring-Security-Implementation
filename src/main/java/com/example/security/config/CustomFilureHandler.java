package com.example.security.config;

import com.example.security.entity.User;
import com.example.security.repository.UserRepo;
import com.example.security.service.UserService;
import com.example.security.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFilureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email=request.getParameter("username");
        User user=userRepo.findByEmail(email);

        if (user!=null){
            if (user.isAccountNonLocked()){
                if (user.getFailedAttempt()< UserServiceImpl.ATTEMPTS-1){
                    userService.increaseFailedAttempt(user);
                }
                else{
                    userService.lock(user);
                    exception=new LockedException("Your Account is now Locked !!");
                }
            }
            else{
                if (userService.unlockAccountTimeExpired(user)){
                    exception=new LockedException("Account is unlocked! Please try to login");
                }
                else{
                    exception=new LockedException("Account is locked! Please try after sometime");
                }

            }
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
