package com.special.controllers;

import com.special.domain.UseCase;
import com.special.services.ActorServices;
import com.special.services.ProjectServices;
import org.springframework.stereotype.Controller; //hmmm
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//allo ena import

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	private 

    private final ProjectServices projectServices; //it needs a constructor on service department
    //private final ActorServices actorServices;//to christiana
    //private final DiagramService diagramService;
    //and another one for diagram to theodora


    public ProjectController(ProjectServices projectServices) { //for initialization
        this.projectServices = projectServices; //dont forget to add the other 2
        //this.diagramService = diagramService;
        //this.userService = userService;
    }



}
