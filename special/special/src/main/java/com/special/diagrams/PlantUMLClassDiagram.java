package com.special.diagrams;

import com.special.domain.CRCCard;
import com.special.domain.Project;

public class PlantUMLClassDiagram extends AbstractClassDiagram {

	 	@Override
	    protected String createHeader(Project project) {
	 		
	        return "@startuml\ntitle " + project.getName() + " - Class Diagram\n\n";
	    }

	    @Override
	    protected String createClass(CRCCard card) {
	    	
	        StringBuilder sb = new StringBuilder();
	        sb.append("class ").append(sanitize(card.getClassName())).append(" {\n");
	        if (card.getResponsibilities() != null && !card.getResponsibilities().isBlank()) {
	            for (String resp : card.getResponsibilities().split("\n")) {
	                sb.append("  ").append(resp.trim()).append("\n");
	            }
	        }
	        sb.append("}\n\n");
	        return sb.toString();
	    }

	    @Override
	    protected String createAssociations(Project project) {
	        StringBuilder sb = new StringBuilder();
	        for (CRCCard card : project.getCRCCards()) {
	            if (card.getCollaborations() != null && !card.getCollaborations().isBlank()) {
	                for (String collab : card.getCollaborations().split("\n")) {
	                    String collaborator = collab.trim();
	                    if (!collaborator.isEmpty()) {
	                        sb.append(sanitize(card.getClassName())).append(" --> ").append(sanitize(collaborator)).append("\n");
	                    }
	                }
	            }
	        }
	        return sb.toString();
	    }

	    @Override
	    protected String createFooter() {
	        return "\n@enduml\n";
	    }

		private String sanitize(String name) {
	        return name.replaceAll("[^a-zA-Z0-9]", "_");
	    }
}
