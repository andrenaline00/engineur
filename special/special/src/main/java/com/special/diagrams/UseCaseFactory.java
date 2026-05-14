package com.special.diagrams;

import org.springframework.stereotype.Component;

@Component
public class UseCaseFactory {

    public UseCaseStrategy create(String tool) {
    	
        return switch (tool.toLowerCase()) {
        
            case "plantuml" -> new PlantUMLUseCaseDiagram();
            case "nomnoml" -> new NomnomlUseCaseDiagram();
            default -> throw new IllegalArgumentException("Unsupported UML tool: " + tool);
            
        };
    }
}
