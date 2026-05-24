package com.special.services;

import org.springframework.stereotype.Service;

import com.special.diagrams.ClassDiagramFactory;
import com.special.diagrams.UseCaseFactory;
import com.special.domain.Project;

	@Service
	public class DiagramService {

	    private final UseCaseFactory useCaseFactory;
	    private final ClassDiagramFactory classFactory;
	    private final ProjectServices projectService;

	    public DiagramService(UseCaseFactory useCaseDiagramFactory,
	                          ClassDiagramFactory classDiagramFactory,
	                          ProjectServices projectService) {
	        this.useCaseFactory = useCaseDiagramFactory;
	        this.classFactory = classDiagramFactory;
	        this.projectService = projectService;
	    }

	    public String generateUseCaseDiagram(Long projectId, String tool) {
	    	
	        Project project = projectService.getProject(projectId);
	        return useCaseFactory.create(tool).create(project);
	    }

	    public String generateClassDiagram(Long projectId, String tool) {
	    	
	        Project project = projectService.getProject(projectId);
	        return classFactory.create(tool).create(project);
	    }
	}



