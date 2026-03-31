package com.special.controllers;

import com.special.domain.User;
import com.special.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // html
    }

    // REGISTER POST
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // TODO: Get current user
        // User currentUser = ...;
        // model.addAttribute("user", currentUser);
        return "dashboard";
    }

    // UPDATE PROFILE PAGE
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // TODO: Get current user
        // User currentUser = ...;
        // model.addAttribute("user", currentUser);
        return "profile";
    }

    // UPDATE PROFILE POST
    @PostMapping("/profile")
    public String updateProfile(@RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            Model model) {

        try {
            User updatedUser = userService.updateProfile(id, username, email, password);
            model.addAttribute("success", "Profile updated successfully!");
            model.addAttribute("user", updatedUser);
            return "profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "profile";
        }
    }
}
