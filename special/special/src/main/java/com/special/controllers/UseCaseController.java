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
@RequestMapping("")
public class UseCaseController { // to create,view,delete,update a use case

    private final ProjectServices projectServices; // pedio

    // constructor
    public UseCaseController(ProjectServices projectServices) {
        this.projectServices = projectServices;
    }

    @GetMapping("/new") // to fetch data
    public String newUseCase(@PathVariable Long projectID, Model model) {
        model.addAttribute("project", projectServices.getProject(projectID));
        model.addAttribute("actors", projectServices.getActors(projectID));
        return "usecase/create"; // we have a usecase section
    }

    @PostMapping // to create new resource
    public String createNewUseCase(@PathVariable Long projectID, @RequestParam String title,
            @RequestParam(required = false) String preconditions,
            @RequestParam(required = false) String mainFlow, @RequestParam(required = false) String altFlows,
            @RequestParam(required = false) String postconditions,
            @RequestParam(required = false) List<Long> actorIDs, RedirectAttributes redirectAttributes) {
        projectServices.createNewUseCase(title, preconditions, mainFlow, altFlows, postconditions, actorIDs, projectID);
        return "";
    }

    @GetMapping() // add path to fetch data
    public String editUseCase(@PathVariable Long projectID, @PathVariable Long ID, Model model) {
        model.addAttribute("project", projectServices.getProject(projectID));
        model.addAttribute("useCase", projectServices.getUseCase(ID));
        model.addAttribute("actors", projectServices.getActors(projectID));
        return "usecases/edit";
    }

    @PostMapping("/{id}")
    public String updateUseCase(@PathVariable Long projectID, @PathVariable Long ID, @RequestParam String title,
            @RequestParam(required = false) String preconditions, @RequestParam(required = false) List<Long> actorIDs,
            @RequestParam(required = false) String mainFlow, @RequestParam(required = false) String altFlows,
            @RequestParam(required = false) String postConditions, RedirectAttributes redirectAttributes) {
        projectServices.updateUseCase(ID, title, preconditions, mainFlow, postConditions, altFlows, actorIDs);
        redirectAttributes.addFlashAttribute("success", "use case updated succesfully");
        return "redirect:/projects" + projectID;
    }

    @PostMapping("/{id}/delete")
    public String deleteUseCase(@PathVariable Long projectID, @PathVariable Long ID,
            RedirectAttributes redirectAttributes) {
        projectServices.deleteUseCase(ID);
        redirectAttributes.addFlashAttribute("Success", "Use Case Deleted Successfully");
        return "redirect:/projects/" + projectID;
    }

}
