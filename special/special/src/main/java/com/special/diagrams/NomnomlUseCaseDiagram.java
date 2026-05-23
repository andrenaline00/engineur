package com.special.diagrams;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.UseCase;

public class NomnomlUseCaseDiagram extends AbstractUseCaseDiagram {
	
    @Override
    protected String createHeader(Project project) {
        return "#title: " + project.getName() + " - Use Case Diagram\n\n";
    }

    @Override
    protected String createActor(Actor actor) {
        return "[<actor> " + actor.getName() + "]\n";
    }

    @Override
    protected String createUseCase(UseCase useCase) {
        return "[<usecase> " + useCase.getTitle() + "]\n";
    }

    @Override
    protected String createAssociation(Actor actor, UseCase useCase) {
        return "[<actor> " + actor.getName() + "] -> [<usecase> " + useCase.getTitle() + "]\n";
    }

    @Override
    protected String createFooter() {
        return "";
    }

}
