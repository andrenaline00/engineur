package com.special.diagrams;

import com.special.domain.CRCCard;
import com.special.domain.Project;

public class NomnomlClassDiagram extends AbstractClassDiagram {
	
	 	@Override
	    protected String createHeader(Project project) {
		 
	        return "#title: " + project.getName() + " - Class Diagram\n\n";
	    }

	    @Override
	    protected String createClass(CRCCard card) {
	    	
	        StringBuilder sb = new StringBuilder();
	        sb.append("[").append(card.getClassName());
	        
	        if (card.getResponsibilities() != null && !card.getResponsibilities().isBlank()) {
	            sb.append("|\n");
	            for (String resp : card.getResponsibilities().split("\n")) {
	                sb.append("  ").append(resp.trim()).append("\n");
	            }
	        }
	        sb.append("]\n");
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
	                        sb.append("[").append(card.getClassName()).append("] -> [")
	                          .append(collaborator).append("]\n");
	                    }
	                }
	            }
	        }
	        return sb.toString();
	    }

	    @Override
	    protected String createFooter() {
	        return "";
	    }

}
