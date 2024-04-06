package com.example.security.controller;


import java.security.Principal;

import com.example.security.entity.User;
import com.example.security.repository.UserRepo;
import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @ModelAttribute
    /* When @ModelAttribute is used at the method level, it indicates that the annotated method should be executed
       before any handler methods in the controller. The return value of this method is then added to the model under a
       specific attribute name. Toh baar baar code likhkr Principal se user get krne ki jagah ek baar mai code krdiya
       */
    /*
      Principal is used to get the username(email in our case) of the user
      */
    public void commonUser(Principal p, Model m) {
        if (p != null) {
            // get the email
            String email = p.getName();
            // the the user through email
            User user = userRepo.findByEmail(email);
            // add the current user to model
            m.addAttribute("user", user);
        }

    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session, Model m) {

        System.out.println(user);

        User u = userService.saveUser(user);

        if (u != null) {
            // System.out.println("save sucess");
            session.setAttribute("msg", "Registered successfully");
        } else {
            // System.out.println("error in server");
            session.setAttribute("msg", "Something wrong server");
        }
        return "redirect:/register";
    }

}
