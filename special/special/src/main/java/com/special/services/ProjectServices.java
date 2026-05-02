package com.special.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.special.domain.CRCCard;
import com.special.repositories.CRCCardRepo;
import com.special.repositories.ProjectRepo;


////////////////////////////////////////////////////////////////////////
//KSEROUME AN XREIAZETAI INTERFACE TO SERVICE???????KALHSPERA
@Service
public class ProjectServices {
	
	private  ProjectRepo projectRepo;
	private  CRCCardRepo crcCardRepo;
	
	//CRC card service
	public List<CRCCard> getCRCCards(Long projectId) {
	        return crcCardRepo.findByProjectId(projectId);
	}
	
	public CRCCard createCRCcard() {
		//get project name 
		//CRCCard card = newCRCCard()
	}
	
	public CRCCard updateCRCcard()
	
	public void deleteCRCcard(Long cardId) {
		crcCardRepo.deleteById(cardId);
	}
}
