package com.special.diagrams;

import org.springframework.stereotype.Component;


@Component
public class ClassDiagramFactory {

    public ClassStrategy create(String tool) {
        return switch (tool.toLowerCase()) {
            case "plantuml" -> new PlantUMLClassDiagram();
            case "nomnoml" -> new NomnomlClassDiagram();
            default -> throw new IllegalArgumentException("Unsupported UML tool: " + tool);
        };
    }
}
