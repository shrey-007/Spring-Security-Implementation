package com.example.security.service;



import com.example.security.entity.User;
import com.example.security.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {

        // before saving user into database you you have to encode the password
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        // before saving user into database you you have to set its role
        user.setRole("ROLE_USER");

        User newuser = userRepo.save(user);

        return newuser;
    }

    @Override
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }

}
