package com.example.security.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    // Since isko yaha autowire kara hai toh iska object bhi banana padega inject krne ke liye toh iski class mai @Component lagado
    public CustomAuthSucessHandler customAuthSucessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    // deprecated methods ki warning aaegi toh unhe hatane ke liye SuppressWarnings use kra
    @SuppressWarnings("all")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
//        http.csrf().disable()
//                .authorizeHttpRequests().requestMatchers("/","/register","/signin","/saveUser").permitAll()
//                .requestMatchers("/user/**").authenticated().and()
//                .formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
//                //.usernameParameter("email")
//                .defaultSuccessUrl("/user/profile").permitAll();
//        return http.build();
        /*
        .authorizeHttpRequests(): This begins the configuration for authorization rules.
        .requestMatchers("/","/register","/signin","/saveUser").permitAll(): Here, it specifies that requests matching the given patterns ("/", "/register", "/signin", "/saveUser") are permitted to be accessed by all users without requiring authentication. This is often used for public pages like home, registration, and sign-in pages.
        .requestMatchers("/user/**").authenticated(): Requests matching the pattern "/user/" are required to be authenticated (i.e., users must be logged in) to access them. The "/user/" pattern typically represents resources or endpoints related to user-specific functionality or areas requiring authentication.
        .and(): This is a chaining method used to concatenate multiple configurations.
        .formLogin().loginPage("/signin").loginProcessingUrl("/userLogin"): This configures form-based authentication. It specifies the login page ("/signin") that users will be redirected to if they attempt to access a protected resource without being authenticated. The loginProcessingUrl() method specifies the URL where the login form data should be submitted for processing.
        .defaultSuccessUrl("/user/profile").permitAll(): After successful authentication, users are redirected to "/user/profile". The permitAll() method allows all users, whether authenticated or not, to access this URL. This is commonly the default landing page for authenticated users after login.
        return http.build(): This finalizes the configuration and builds the HTTP security configuration, returning it for further use.
        */
        http.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll().and()
                .formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
                // Ye customAuthSucessHandler ka object hume bataege ki konse role ke user ko kaha redirect krna hai successful login ke baad toh iski class implement krni padegi, pehle direcly defaultSuccessUrl() use kr rhe the but voh saare users ko same jagah bhejega login ke baad isliye ye use kara
                .successHandler(customAuthSucessHandler)
                .permitAll();
        return http.build();
    }

}
