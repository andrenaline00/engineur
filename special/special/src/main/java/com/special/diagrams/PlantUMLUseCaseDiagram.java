package com.special.diagrams;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.UseCase;

public class PlantUMLUseCaseDiagram extends AbstractUseCaseDiagram {
	
		@Override
	    protected String createHeader(Project project) {
			
	        return "@startuml\ntitle " + project.getName() + " - Use Case Diagram\n\n";
	    }

	    @Override
	    protected String createActor(Actor actor) {
	    	
	        return "actor \"" + actor.getName() + "\" as " + sanitize(actor.getName()) + "\n";
	    }

	    @Override
	    protected String createUseCase(UseCase useCase) {
	    	
	        return "usecase \"" + useCase.getTitle() + "\" as " + sanitize(useCase.getTitle()) + "\n";
	    }

	    @Override
	    protected String createAssociation(Actor actor, UseCase useCase) {
	    	
	        return sanitize(actor.getName()) + " --> " + sanitize(useCase.getTitle()) + "\n";
	    }

	    @Override
	    protected String createFooter() {
	    	
	        return "\n@enduml\n";
	    }

	    private String sanitize(String name) {
	    	
	        return name.replaceAll("[^a-zA-Z0-9]", "_");
	    }

}
