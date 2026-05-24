package com.special.controllers;

import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.ProjectServices;
import com.special.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Transactional
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServices projectServices;
    private final UserService userService;

    public ProjectController(ProjectServices projectServices, UserService userService) {
        this.projectServices = projectServices;
        this.userService = userService;
    }

    private User resolveUser(Object principal) {
        if (principal == null) {
            throw new RuntimeException("No authenticated user found");
        }
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return (User) userService.loadUserByUsername(username);
    }

    @GetMapping
    public String listProjects(@AuthenticationPrincipal Object principal, Model model) {
        User currentUser = resolveUser(principal);
        model.addAttribute("projects", projectServices.getProjectsForUser(currentUser.getId()));
        return "project/list";
    }

    @GetMapping("/new")
    public String newProjectForm() {
        return "project/new";
    }

    @PostMapping
    public String createProject(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {

        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        User user = resolveUser(auth != null ? auth.getPrincipal() : null);

        String finalDescription = (description != null) ? description : "";
        projectServices.createProject(name, finalDescription, user.getId());

        redirectAttributes.addFlashAttribute("success", "Project created.");
        return "redirect:/project/" ;
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectServices.getProject(id);
        model.addAttribute("project", project);
        model.addAttribute("useCases", projectServices.getUseCases(id));
        model.addAttribute("actors", projectServices.getActors(id));
        model.addAttribute("crcCards", projectServices.getCrcCards(id));
        return "project/details";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectServices.deleteProject(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted.");
        return "redirect:/project";
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
