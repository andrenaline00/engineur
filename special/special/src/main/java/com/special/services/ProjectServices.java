package com.special.services;

import com.special.domain.*;
import com.special.repositories.ActorRepo;
import com.special.repositories.CRCCardRepo;
import com.special.repositories.ProjectRepo;
import com.special.repositories.UseCaseRepo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ProjectServices {
    private final ProjectRepo projectRepo;
    private final UseCaseRepo useCaseRepo;
    //private final CRCCard crcCard;
    private final CRCCardRepo crcCardRepo;
    //gia actor repo
    private final ActorRepo actorRepo;
    //gia diagram repo
    // gia crc repo

    public ProjectServices(ProjectRepo projectRepo, UseCaseRepo useCaseRepo, CRCCardRepo crcCardRepo, ActorRepo actorRepo) {
        this.projectRepo = projectRepo;
        this.useCaseRepo = useCaseRepo;
        this.crcCardRepo = crcCardRepo;
        //same for the other repos
        this.actorRepo = actorRepo;
    }

    public Project getProject(Long projectID){
        //return projectRepo.findById(projectID);
        return null;
    }


    public List<Project> getProjectsForUser(Long userID){
        return projectRepo.findByUserID(userID);
        //return null;
    }

    //for projects
    @Transactional
    public Project createProject(String name, String description,User user){
        Project project = new Project(name,description,user);
        return projectRepo.save(project);
    }

    @Transactional
    public void deleteProject(Long projectID){
        //delete all related use cases,actors,diagrams,crc cards
        projectRepo.deleteById(projectID);
        useCaseRepo.deleteByProjectID(projectID);
        actorRepo.deleteByProjectID(projectID);
        //diagramRepo.deleteByProjectID(projectID);
        //crcRepo.deleteByProjectID(projectID);

    }

    //for actors
    @Transactional
    public List<Actor> getActors(Long projectID){
        return actorRepo.findByProjectID(projectID);
        //return null;
    }

    public Actor getActor(Long actorID){
        return actorRepo.findById(actorID).orElseThrow();
        //return null;
    }

    @Transactional
    public Actor createActor(String name,String description,Long projectID){
        Project project=getProject(projectID);
        Actor actor = new Actor(name,description,project);
        return actorRepo.save(actor);
        //return null;
    }

    @Transactional
    public Actor updateActor(Long actorID, String name, String description){
        Actor actor =getActor(actorID);
        actor.setName(name);
        return actorRepo.save(actor);
        //return null;
    }

    @Transactional
    public void deleteActor(Long actorID){
        Actor actor=getActor(actorID);
        //delete all related use cases
        for (UseCase useCase:actor.getUseCases()){
            useCase.getActors().remove(actor);
            useCaseRepo.save(useCase);
        }
        actorRepo.deleteById(actorID);
    }

    //for usecases
    public List<UseCase> getUseCase(Long projectID){
        return useCaseRepo.findByProjectID(projectID);
    }

    public UseCase getUseCases(Long useCaseID) {
        return useCaseRepo.findById(useCaseID).orElseThrow();
    }

    @Transactional
    public UseCase createNewUseCase(String title,String preconditions,String mainFlow,String altFlows,String postconditions,List<Long> actorIDs,Long projectID){
        Project project = projectRepo.findById(projectID).orElseThrow();

        UseCase useCase = new UseCase(title,project);
        useCase.setPreconditions(preconditions);
        useCase.setMainFlow(mainFlow);
        useCase.setAltFlows(altFlows);
        useCase.setPostconditions(postconditions);
        //orrrr
        /*if (actorIDs != null && actorIDs.isEmpty()){
            useCase.setActors(new HashSet<>(actorRepo.));
        }*/

        for (Long actorID:actorIDs){
            Actor actor = getActor(actorID);
            useCase.getActors().add(actor);
        }
        return useCaseRepo.save(useCase);

    }

    @Transactional
    public UseCase updateUseCase(Long useCaseID,String title,String preconditions,String mainFlow,String postconditions,String altFlows,List<Long> actorIDs) {
        UseCase useCase = getUseCases(useCaseID);
        useCase.setTitle(title);
        useCase.setPreconditions(preconditions);
        useCase.setMainFlow(mainFlow);
        useCase.setAltFlows(altFlows);
        useCase.setPostconditions(postconditions);

        //hashset me actors
        Set<Actor> actors = new HashSet<>();

        useCase.setActors(actors);
        return useCaseRepo.save(useCase);
    }

    @Transactional
    public void deleteUseCase(Long useCaseID){
        UseCase useCase = getUseCases(useCaseID);
        //delete all related actors
        useCase.getActors().clear();
        /*
        for (CRCCard card:useCase.getCRCCards()) {
            card.getUseCases().remove(useCase);
        }*/
        useCaseRepo.deleteById(useCaseID);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CRC cards services

    public List<CRCCard> getCrcCards(Long projectId) {
        return crcCardRepo.findByProjectId(projectId);
        
    }

    public CRCCard getCrcCard(Long cardId) {
        return crcCardRepo.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("CRC Card not found"));
    }

    @Transactional
    public CRCCard createCrcCard(String className, String responsibilities,
                                  String collaborations, List<Long> useCaseIds,Long projectID) {
        Project project = getProject(projectID);
        CRCCard card = new CRCCard(className, project);
        card.setResponsibilities(responsibilities);
        card.setCollaborations(collaborations);
        
        if (useCaseIds != null && !useCaseIds.isEmpty()) {
            Set<UseCase> useCases = new HashSet<>();
            for (Long useCaseId : useCaseIds) {
                useCases.add(getUseCases(useCaseId));
            }
            card.setUseCases(useCases);
        }
        
        return crcCardRepo.save(card);
    }

    @Transactional
    public CRCCard updateCrcCard(Long cardId, String className, String responsibilities,
                                  String collaborations, List<Long> useCaseIds) {
    	
        CRCCard card = getCrcCard(cardId);
        card.setClassname(className);
        card.setResponsibilities(responsibilities);
        card.setCollaborations(collaborations);
        
        Set<UseCase> useCases = new HashSet<>();
        if (useCaseIds != null && !useCaseIds.isEmpty()) {
            for (Long useCaseId : useCaseIds) {
                useCases.add(getUseCases(useCaseId));
            }
        }
        card.setUseCases(useCases);
        
        return crcCardRepo.save(card);
    }

    @Transactional
    public void deleteCrcCard(Long cardID) {
        crcCardRepo.deleteById(cardID);
	}


}
