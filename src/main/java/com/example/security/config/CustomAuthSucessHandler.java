package com.example.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthSucessHandler implements AuthenticationSuccessHandler {
    // Ye configuration class hai jisme hum bataege ki konse role ke user ko kaha redirect krna hai successful login ke baad
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // authentication object ke paas list of authority hai current user ki
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if(roles.contains("ROLE_ADMIN")){
            response.sendRedirect("/admin/profile");
        }
        else{
            response.sendRedirect("/user/profile");
        }

        // Now if user try to access url starting from /admin then it will give 403 response(forbidden)


    }
}
