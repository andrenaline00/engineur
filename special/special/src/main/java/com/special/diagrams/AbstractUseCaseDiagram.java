package com.special.diagrams;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.UseCase;

public abstract class AbstractUseCaseDiagram implements UseCaseStrategy{
	 @Override
	    public final String generate(Project project) {
	        StringBuilder sb = new StringBuilder();
	        sb.append(generateHeader(project));
	        for (Actor actor : project.getActors()) {
	            sb.append(generateActor(actor));
	        }
	        for (UseCase useCase : project.getUseCases()) {
	            sb.append(generateUseCase(useCase));
	        }
	        for (UseCase useCase : project.getUseCases()) {
	            for (Actor actor : useCase.getActors()) {
	                sb.append(generateAssociation(actor, useCase));
	            }
	        }
	        sb.append(generateFooter());
	        return sb.toString();
	    }

	    protected abstract String generateHeader(Project project);
	    protected abstract String generateActor(Actor actor);
	    protected abstract String generateUseCase(UseCase useCase);
	    protected abstract String generateAssociation(Actor actor, UseCase useCase);
	    protected abstract String generateFooter();

}
