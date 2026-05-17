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
@RequestMapping("projects/{projectID}/crccards")
public class CRCCardController {

	private ProjectServices projectService;

	public CRCCardController(ProjectServices projectService) {
		this.projectService = projectService;
	}

	@GetMapping("/new")
	public String newCrcCardForm(@PathVariable Long projectID, Model model) {

		// public String newCRCcard(Model model, @PathVariable ProjectServices
		// projectservice, @PathVariable Long projectID) {

		model.addAttribute("project", projectService.getProject(projectID));
		model.addAttribute("useCases", projectService.getUseCases(projectID));

		return "crccards/create";

	}

	@PostMapping
	public String createCRCcard(@PathVariable Long projectID,
			@RequestParam String className,
			@RequestParam(required = false) String responsibilities,
			@RequestParam(required = false) String collaborations,
			@RequestParam(required = false) List<Long> useCaseIds,
			RedirectAttributes redirectAttribute) {

		projectService.createCrcCard(className, responsibilities, collaborations, useCaseIds, projectID);
		redirectAttribute.addFlashAttribute("success", "CRC Card created.");

		return "redirect:/projects/" + projectID;
	}

	@PostMapping("/{id}")
	public String updateCrcCard(@PathVariable Long projectID,
			@PathVariable Long id,
			@RequestParam String className,
			@RequestParam(required = false) String responsibilities,
			@RequestParam(required = false) String collaborations,
			@RequestParam(required = false) List<Long> useCaseIds,
			RedirectAttributes redirectAttributes) {

		projectService.updateCrcCard(id, className, responsibilities, collaborations, useCaseIds);
		redirectAttributes.addFlashAttribute("success", "CRC Card updated.");

		return "redirect:/projects/" + projectID;
	}

	@GetMapping("/{id}/edit")
	public String editCRCcard(@PathVariable Long projectID,
			@PathVariable Long id,
			Model model) {

		model.addAttribute("project", projectService.getProject(projectID));
		model.addAttribute("crcCard", projectService.getCrcCard(id));
		model.addAttribute("useCases", projectService.getUseCases(projectID));

		return "crccards/edit";
	}

	@PostMapping("/{id}/delete")
	public String deleteCRCcard(@PathVariable Long projectID,
			@PathVariable Long id, RedirectAttributes redirectAttributes) {

		projectService.deleteCrcCard(id);
		redirectAttributes.addFlashAttribute("success", "CRC Card deleted.");

		return "redirect:/projects/" + projectID;
	}

}
