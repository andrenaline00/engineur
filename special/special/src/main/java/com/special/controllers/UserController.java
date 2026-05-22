package com.special.controllers;

import com.special.domain.User;
import com.special.services.ProjectServices;
import com.special.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;
    private final ProjectServices projectServices;

    public UserController(UserService userService, ProjectServices projectServices) {
        this.userService = userService;
        this.projectServices = projectServices;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }


    // REGISTER PAGE
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // REGISTER POST
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirect) {

        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());

            model.addAttribute("success", "Registration successful! Please login.");
            return "auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";

        }
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("projects", projectServices.getProjectsForUser(user.getId()));
        return "layout/dashboard";
    }

    // UPDATE PROFILE PAGE
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/profile";
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
            return "user/profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/profile";
        }
    }
}
