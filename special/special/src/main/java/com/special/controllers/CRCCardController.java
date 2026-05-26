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
@RequestMapping("projects/{projectId}/crccards")
public class CRCCardController {

	private ProjectServices projectService;

	public CRCCardController(ProjectServices projectService) {
		this.projectService = projectService;
	}

	
	@GetMapping("/new")
	public String newCrcCardForm(@PathVariable("projectId") Long projectId, Model model) {

		model.addAttribute("project", projectService.getProject(projectId));
		model.addAttribute("useCases", projectService.getUseCases(projectId));

		return "crccards/create";
	}

	@PostMapping
	public String createCRCcard(@PathVariable Long projectId,
			@RequestParam String className,
			@RequestParam(required = false) String responsibilities,
			@RequestParam(required = false) String collaborations,
			@RequestParam(required = false) List<Long> useCaseIds,
			RedirectAttributes redirectAttribute) {

		projectService.createCrcCard(className, responsibilities, collaborations, useCaseIds, projectId);
		redirectAttribute.addFlashAttribute("success", "CRC Card created.");

		return "redirect:/projects/" + projectId;
	}

	@PostMapping("/{id}")
	public String updateCrcCard(@PathVariable Long projectId,
			@PathVariable Long id,
			@RequestParam String className,
			@RequestParam(required = false) String responsibilities,
			@RequestParam(required = false) String collaborations,
			@RequestParam(required = false) List<Long> useCaseIds,
			RedirectAttributes redirectAttributes) {

		projectService.updateCrcCard(id, className, responsibilities, collaborations, useCaseIds);
		redirectAttributes.addFlashAttribute("success", "CRC Card updated.");

		return "redirect:/projects/" + projectId;
	}

	@GetMapping("/{id}/edit")
	public String editCRCcard(@PathVariable Long projectId,
			@PathVariable Long id,
			Model model) {

		model.addAttribute("project", projectService.getProject(projectId));
		model.addAttribute("crcCard", projectService.getCrcCard(id));
		model.addAttribute("useCases", projectService.getUseCases(projectId));

		return "crccards/edit";
	}
	
	 @GetMapping
	    public String listCRCcards(@PathVariable("projectId") Long projectId, Model model) {
		 	model.addAttribute("project", projectService.getProject(projectId));
	        model.addAttribute("crccards", projectService.getCrcCards(projectId));
	        return "crccards/list"; 
	    }

	@PostMapping("/{id}/delete")
	public String deleteCRCcard(@PathVariable Long projectId,
			@PathVariable Long id, RedirectAttributes redirectAttributes) {

		projectService.deleteCrcCard(id);
		redirectAttributes.addFlashAttribute("success", "CRC Card deleted.");

		return "redirect:/projects/" + projectId;
	}

}
