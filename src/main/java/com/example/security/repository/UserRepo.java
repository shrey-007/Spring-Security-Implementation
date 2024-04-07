package com.example.security.repository;



import com.example.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface UserRepo extends JpaRepository<User, Integer> {

    public User findByEmail(String emaill);

    // This is for limitting number of attempts for login by user
    @Query("update User u set u.failedAttempt=?1 where email=?2")
    @Modifying
    public void updateFailedAttempt(int attempt,String email);

}
