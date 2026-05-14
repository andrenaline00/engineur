package com.special.controllers;

import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.ProjectServices;
import com.special.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServices projectServices;
    private final UserService userService;

    public ProjectController(ProjectServices projectServices, UserService userService) {
        this.projectServices = projectServices;
        this.userService = userService;
    }

    private User resolveUser(UserDetails principal) {
        return (User) userService.loadUserByUsername(principal.getUsername());
    }

    @GetMapping
    public String listProjects(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = resolveUser(principal);
        model.addAttribute("projects", projectServices.getProjectsForUser(user.getId()));
        return "projects/list";
    }

    @GetMapping("/new")
    public String newProjectForm() {
        return "projects/create";
    }

    @PostMapping
    public String createProject(@AuthenticationPrincipal UserDetails principal,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        User user = resolveUser(principal);
        projectServices.createProject(name, description, user);
        redirectAttributes.addFlashAttribute("success", "Project created.");
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectServices.getProject(id);
        model.addAttribute("project", project);
        model.addAttribute("useCases", projectServices.getUseCase(id));
        model.addAttribute("actors", projectServices.getActors(id));
        model.addAttribute("crcCards", projectServices.getCrcCards(id));
        return "projects/view";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectServices.deleteProject(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted.");
        return "redirect:/projects";
    }

    // Diagram features are currently disabled as DiagramService is not yet
    // implemented.
    /*
     * @GetMapping("/{id}/diagrams/usecase")
     * public String useCaseDiagram(@PathVariable Long id,
     * 
     * @RequestParam(defaultValue = "plantuml") String tool,
     * Model model) {
     * // Implementation pending
     * return "projects/diagram";
     * }
     */
}
