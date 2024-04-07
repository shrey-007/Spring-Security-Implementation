package com.example.security.service;



import com.example.security.entity.User;
import com.example.security.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final long LOCK_DURATION_TIME=300000;

    public static final long ATTEMPTS=3;

    @Override
    public User saveUser(User user) {

        // before saving user into database you you have to encode the password
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        // before saving user into database you you have to set its role, and other default attributes
        user.setRole("ROLE_USER");
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setLockTime(null);

        User newuser = userRepo.save(user);

        return newuser;
    }

    @Override
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }

    //These three new methods  is for limiting number of login by user
    @Override
    public void increaseFailedAttempt(User user) {
        int attempt=user.getFailedAttempt()+1;
        userRepo.updateFailedAttempt(attempt,user.getEmail());
    }

    @Override
    public void resetAttempt(String email) {
        userRepo.updateFailedAttempt(0,email);
    }

    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepo.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(User user) {
        long lockTimeInMills = user.getLockTime().getTime();
        long currentTimeMillis=System.currentTimeMillis();

        if (lockTimeInMills+LOCK_DURATION_TIME<currentTimeMillis){
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepo.save(user);
            return true;
        }
        return false;
    }
}
