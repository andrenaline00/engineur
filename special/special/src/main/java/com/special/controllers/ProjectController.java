package com.special.controllers;


import org.springframework.stereotype.Controller; //hmmm
import org.springframework.web.bind.annotation.*;
//allo ena import

import com.special.services.ProjectServices;


@Controller
@RequestMapping("/project")
public class ProjectController {
	

    private  ProjectServices projectServices; //it needs a constructor on service department
    //private final ActorServices actorServices;//to christiana
    //private final DiagramService diagramService;
    //and another one for diagram to theodora


    public ProjectController(ProjectServices projectServices) { //for initialization
        this.projectServices = projectServices; //dont forget to add the other 2
        //this.diagramService = diagramService;
        //this.userService = userService;
    }



}
