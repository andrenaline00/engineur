package com.special.diagrams;


public  UseCaseFactory {
	
	public UseCaseDiagramStrategy create(String tool) {
        return switch (tool.toLowerCase()) {
            case "plantuml" -> new PlantUmlUseCaseDiagramGenerator();
            case "nomnoml" -> new NomnomlUseCaseDiagramGenerator();
            default -> throw new IllegalArgumentException("Unsupported UML tool: " + tool);
        };
    }

}
