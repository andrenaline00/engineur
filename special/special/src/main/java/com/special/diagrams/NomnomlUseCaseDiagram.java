package com.special.diagrams;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.UseCase;

public class NomnomlUseCaseDiagram extends AbstractUseCaseDiagram {
	
    @Override
    protected String generateHeader(Project project) {
        return "#title: " + project.getName() + " - Use Case Diagram\n\n";
    }

    @Override
    protected String generateActor(Actor actor) {
        return "[<actor> " + actor.getName() + "]\n";
    }

    @Override
    protected String generateUseCase(UseCase useCase) {
        return "[<usecase> " + useCase.getTitle() + "]\n";
    }

    @Override
    protected String generateAssociation(Actor actor, UseCase useCase) {
        return "[<actor> " + actor.getName() + "] -> [<usecase> " + useCase.getTitle() + "]\n";
    }

    @Override
    protected String generateFooter() {
        return "";
    }

}
