package com.special.controllers;

//import com.special.domain.UseCase;
import com.special.services.ProjectServices;
//import com.special.services.ActorServices;

import org.springframework.stereotype.Controller; //framework for every controller class
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; //annotations in general
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectID}/usecases")
public class UseCaseController { // to create,view,delete,update a use case

    private final ProjectServices projectServices; // pedio

    // constructor
    public UseCaseController(ProjectServices projectServices) {
        this.projectServices = projectServices;
    }


    @GetMapping
    public String listUseCases(@PathVariable("projectID") Long projectId, Model model) {
        model.addAttribute("project", projectServices.getProject(projectId));
        model.addAttribute("useCases", projectServices.getUseCases(projectId));
        return "usecases/view"; // we have a usecase section of htmls
    }
    @GetMapping("/new") // to fetch data
    public String newUseCase(@PathVariable("projectID") Long projectId, Model model) {
        model.addAttribute("project", projectServices.getProject(projectId));
        model.addAttribute("actors", projectServices.getActors(projectId));
        return "usecases/create"; // we have a usecase section of htmls
    }

    @PostMapping() // to create new resource
    public String createNewUseCase(@PathVariable("projectID") Long projectId, @RequestParam String title,
            @RequestParam(required = false) String preconditions,
            @RequestParam(required = false) String mainFlow, @RequestParam(required = false) String altFlows,
            @RequestParam(required = false) String postconditions,
            @RequestParam(required = false) List<Long> actorIds, RedirectAttributes redirectAttributes) {
        projectServices.createNewUseCase(title, preconditions, mainFlow, altFlows, postconditions, actorIds, projectId);
        redirectAttributes.addFlashAttribute("success", "Use case created successfully");
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @GetMapping("/{id}/edit") // add path to fetch data
    public String editUseCase(@PathVariable("projectID") Long projectId, @PathVariable Long id, Model model) {
        model.addAttribute("project", projectServices.getProject(projectId));
        model.addAttribute("useCase", projectServices.getUseCase(id));
        model.addAttribute("actors", projectServices.getActors(projectId));
        return "usecases/edit";
    }

    @PostMapping("/{id}")
    public String updateUseCase(@PathVariable("projectID") Long projectId, @PathVariable Long id, @RequestParam String title,
            @RequestParam(required = false) String preconditions, @RequestParam(required = false) List<Long> actorIds,
            @RequestParam(required = false) String mainFlow, @RequestParam(required = false) String altFlows,
            @RequestParam(required = false) String postconditions, RedirectAttributes redirectAttributes) {
        projectServices.updateUseCase(id, title, preconditions, mainFlow, postconditions, altFlows, actorIds);
        redirectAttributes.addFlashAttribute("success", "use case updated succesfully");
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @PostMapping("/{id}/delete")
    public String deleteUseCase(@PathVariable("projectID") Long projectId, @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        projectServices.deleteUseCase(id);
        redirectAttributes.addFlashAttribute("Success", "Use Case Deleted Successfully");
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @GetMapping("/{id}/delete")
    public String deleteUseCaseViaGet(@PathVariable("projectID") Long projectId, @PathVariable Long id) {
        return "redirect:/projects/" + projectId + "/usecases";
    }

}
