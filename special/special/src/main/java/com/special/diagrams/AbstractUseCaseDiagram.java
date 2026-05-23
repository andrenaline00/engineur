package com.special.diagrams;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.UseCase;

public abstract class AbstractUseCaseDiagram implements UseCaseStrategy{
	
	 @Override
	 public final String create(Project project) {
		 
	        StringBuilder sb = new StringBuilder();
	        sb.append(createHeader(project));
	        
	        for (Actor actor : project.getActors()) {
	            sb.append(createActor(actor));
	        }
	        for (UseCase useCase : project.getUseCases()) {
	            sb.append(createUseCase(useCase));
	        }
	        for (UseCase useCase : project.getUseCases()) {
	            for (Actor actor : useCase.getActors()) {
	                sb.append(createAssociation(actor, useCase));
	            }
	        }
	        sb.append(createFooter());
	        return sb.toString();
	    }

	    protected abstract String createHeader(Project project);
	    protected abstract String createActor(Actor actor);
	    protected abstract String createUseCase(UseCase useCase);
	    protected abstract String createAssociation(Actor actor, UseCase useCase);
	    protected abstract String createFooter();

}
