package com.special.controllers;

import com.special.domain.Actor;
import com.special.services.ProjectServices;
import com.special.services.ActorServices;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectID}/actors")
public class ActorController {
    private final ActorServices actorService;
    private final ProjectServices projectService;

    public ActorController(ActorServices actorService, ProjectServices projectService) {
        this.actorService = actorService;
        this.projectService = projectService;
    }

    @GetMapping("/new")
    public String newActor(@PathVariable Long projectID, Model model) {
        model.addAttribute("project", projectService.getProject(projectID));
        return "actors/create";
    }

    @PostMapping
    public String createActor(@PathVariable Long projectID, 
                             @RequestParam String name, 
                             @RequestParam(required = false) String description,
                             RedirectAttributes redirectAttributes) {
        projectService.createActor(name, description, projectID);
        redirectAttributes.addFlashAttribute("success", "Actor created successfully");
        return "redirect:/projects/" + projectID;
    }

    @GetMapping
    public String listActors(@PathVariable Long projectID, Model model) {
        List<Actor> actors = projectService.getActors(projectID);
        model.addAttribute("project", projectService.getProject(projectID));
        model.addAttribute("actors", actors);
        return "actors/list";
    }

    @GetMapping("/{actorID}")
    public String editActor(@PathVariable Long projectID, @PathVariable Long actorID, Model model) {
        model.addAttribute("project", projectService.getProject(projectID));
        model.addAttribute("actor", projectService.getActor(actorID));
        return "actors/edit";
    }

    @PostMapping("/{actorID}")
    public String updateActor(@PathVariable Long projectID, 
                             @PathVariable Long actorID, 
                             @RequestParam String name, 
                             @RequestParam(required = false) String description,
                             RedirectAttributes redirectAttributes) {
        projectService.updateActor(actorID, name, description);
        redirectAttributes.addFlashAttribute("success", "Actor updated successfully");
        return "redirect:/projects/" + projectID;
    }

    @PostMapping("/{actorID}/delete")
    public String deleteActor(@PathVariable Long projectID, 
                             @PathVariable Long actorID, 
                             RedirectAttributes redirectAttributes) {
        projectService.deleteActor(actorID);
        redirectAttributes.addFlashAttribute("success", "Actor deleted successfully");
        return "redirect:/projects/" + projectID;
    }
}
