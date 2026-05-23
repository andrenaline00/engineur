package com.special.diagrams;

import com.special.domain.CRCCard;
import com.special.domain.Project;


public abstract class AbstractClassDiagram implements ClassStrategy {
	
	 @Override
	  public final String create(Project project) {
		 
	        StringBuilder sb = new StringBuilder();
	        sb.append(createHeader(project));
	        
	        for (CRCCard card : project.getCRCCards()) {
	            sb.append(createClass(card));
	        }
	        
	        sb.append(createAssociations(project));
	        sb.append(createFooter());
	        return sb.toString();
	    }

	    protected abstract String createHeader(Project project);
	    protected abstract String createClass(CRCCard card);
	    protected abstract String createAssociations(Project project);
	    protected abstract String createFooter();

}
