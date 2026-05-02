package com.special.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.special.services.ProjectServices;

@Controller
@RequestMapping("projects/{projectname}/crccards")
public class CRCCardController {
	
	private ProjectServices projectService;
	
	@GetMapping("/new")
	public String newCRCcard(Model model, @PathVariable ProjectServices projectservice) {
		
		model.addAttribute("project", projectService.getProject(projectId));
        model.addAttribute("useCases", projectService.getUseCases(projectId));
        
        return "crccards/create";
		
	}
	

	public String createCRCcard(@PathVariable Long projectId) {
		//kalloume to projectService
		return "redirect:/projects/" + projectId;
	}
	
	@PostMapping("/{id}")
	public String updateCRCcard(@PathVariable Long projectId,
            @PathVariable Long id,
            @RequestParam String className,
            @RequestParam(required = false) String responsibilities,
            @RequestParam(required = false) String collaborations,
            @RequestParam(required = false) List<Long> useCaseIds) {
		projectService.updateCRCcard(id,className,responsibilities,collaborations,useCaseIds)
		//redirectAttributes???????????????????????????????????
		return "redirect:/projects/" + projectId;
	}
	
	@GetMapping("/{id}/edit")
	public String editCRCcard(@PathVariable Long projectId,
            @PathVariable Long id,
            Model model) {
		//projectService!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//getProject
		//getCRCCard
		//getUseCases
		return "crccards/edit";
	}
	
	
	 @PostMapping("/{id}/delete")
	public String deleteCRCcard(@PathVariable Long projectId,
            @PathVariable Long id) {
		//projectService
		 projectService.deleteCRCcard(id);
		 //redirectAttributes???????????????????????????????????
		 return "redirect:/projects/" + projectId;
	}

}
