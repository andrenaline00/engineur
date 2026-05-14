package com.special.diagrams;

import com.special.domain.CRCCard;
import com.special.domain.Project;


public abstract class AbstractClassDiagram implements ClassDiagramStrategy {
	 @Override
	    public final String generate(Project project) {
	        StringBuilder sb = new StringBuilder();
	        sb.append(generateHeader(project));
	        for (CRCCard card : project.getCRCCards()) {
	            sb.append(generateClass(card));
	        }
	        sb.append(generateAssociations(project));
	        sb.append(generateFooter());
	        return sb.toString();
	    }

	    protected abstract String generateHeader(Project project);
	    protected abstract String generateClass(CRCCard card);
	    protected abstract String generateAssociations(Project project);
	    protected abstract String generateFooter();

}
