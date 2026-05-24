package com.special.services;

import com.special.domain.*;
import com.special.repositories.ActorRepo;
import com.special.repositories.CRCCardRepo;
import com.special.repositories.ProjectRepo;
import com.special.repositories.UseCaseRepo;
import com.special.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectServices {
    private final ProjectRepo projectRepo;
    private final UseCaseRepo useCaseRepo;
    // private final CRCCard crcCard;
    private final CRCCardRepo crcCardRepo;
    // gia actor repo
    private final ActorRepo actorRepo;
    private final UserRepository userRepository;

    public ProjectServices(ProjectRepo projectRepo, UseCaseRepo useCaseRepo, CRCCardRepo crcCardRepo,
            ActorRepo actorRepo, UserRepository userRepository) {
        this.projectRepo = projectRepo;
        this.useCaseRepo = useCaseRepo;
        this.crcCardRepo = crcCardRepo;
        this.actorRepo = actorRepo;
        this.userRepository = userRepository;
    }

    public Project getProject(Long projectID) {
        return projectRepo.findById(projectID).orElseThrow();
        // return null;
    }

    public List<Project> getProjectsForUser(Long userID) {
        return projectRepo.findByUserId(userID);
        // return null;
    }

    // for projects
    @Transactional
    public Project createProject(String name, String description, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Project project = new Project(name, description, user);
        return projectRepo.save(project);
    }

    @Transactional
    public void deleteProject(Long projectID) {
        // cascade = ALL + orphanRemoval on Project handles child deletion automatically
        projectRepo.deleteById(projectID);
    }

    // for actors
    @Transactional
    public List<Actor> getActors(Long projectID) {
        return actorRepo.findByProjectId(projectID);
        // return null;
    }

    public Actor getActor(Long actorID) {
        return actorRepo.findById(actorID).orElseThrow();
        // return null;
    }

    @Transactional
    public Actor createActor(String name, String description, Long projectID) {
        Project project = getProject(projectID);
        Actor actor = new Actor(name, description, project);
        return actorRepo.save(actor);
        // return null;
    }

    @Transactional
    public Actor updateActor(Long actorID, String name, String description) {
        Actor actor = getActor(actorID);
        actor.setName(name);
        actor.setDescription(description);
        return actorRepo.save(actor);
    }

    @Transactional
    public void deleteActor(Long actorID) {
        Actor actor = getActor(actorID);
        // delete all related use cases
        for (UseCase useCase : actor.getUseCases()) {
            useCase.getActors().remove(actor);
            useCaseRepo.save(useCase);
        }
        actorRepo.deleteById(actorID);
    }

    // for usecases
    public List<UseCase> getUseCases(Long projectID) {
        return useCaseRepo.findByProjectId(projectID);
    }

    @Transactional(readOnly = true) //for not crashing
    public UseCase getUseCase(Long useCaseID) {
        return useCaseRepo.findById(useCaseID).orElseThrow();
    }

    @Transactional
    public UseCase createNewUseCase(String title, String preconditions, String mainFlow, String altFlows,
            String postconditions, List<Long> actorIDs, Long projectID) {
        Project project = projectRepo.findById(projectID).orElseThrow();

        UseCase useCase = new UseCase(title, project);
        useCase.setPreconditions(preconditions);
        useCase.setMainFlow(mainFlow);
        useCase.setAltFlows(altFlows);
        useCase.setPostconditions(postconditions);
        // orrrr
        /*
         * if (actorIDs != null && actorIDs.isEmpty()){
         * useCase.setActors(new HashSet<>(actorRepo.));
         * }
         */

        if (actorIDs != null) {
            for (Long actorID : actorIDs) {
                Actor actor = getActor(actorID);
                useCase.getActors().add(actor);
            }
        }
        return useCaseRepo.save(useCase);

    }

    @Transactional
    public UseCase updateUseCase(Long useCaseID, String title, String preconditions, String mainFlow,
            String postconditions, String altFlows, List<Long> actorIDs) {
        UseCase useCase = getUseCase(useCaseID);
        useCase.setTitle(title);
        useCase.setPreconditions(preconditions);
        useCase.setMainFlow(mainFlow);
        useCase.setAltFlows(altFlows);
        useCase.setPostconditions(postconditions);

        Set<Actor> actors = new HashSet<>();
        if (actorIDs != null) {
            for (Long actorID : actorIDs) {
                actors.add(getActor(actorID));
            }
        }
        useCase.setActors(actors);
        return useCaseRepo.save(useCase);
    }

    @Transactional
    public void deleteUseCase(Long useCaseID) {
        UseCase useCase = getUseCase(useCaseID);
        // delete all related actors
        useCase.getActors().clear();
        /*
         * for (CRCCard card:useCase.getCRCCards()) {
         * card.getUseCases().remove(useCase);
         * }
         */
        useCaseRepo.save(useCase);
        useCaseRepo.deleteById(useCaseID);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CRC cards services

    public List<CRCCard> getCrcCards(Long projectId) {
        return crcCardRepo.findByProjectId(projectId);

    }

    public CRCCard getCrcCard(Long cardId) {
        return crcCardRepo.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("CRC Card not found"));
    }

    @Transactional
    public CRCCard createCrcCard(String className, String responsibilities,
            String collaborations, List<Long> useCaseIds, Long projectID) {
        Project project = getProject(projectID);
        CRCCard card = new CRCCard(className, project);
        card.setResponsibilities(responsibilities);
        card.setCollaborations(collaborations);

        if (useCaseIds != null && !useCaseIds.isEmpty()) {
            Set<UseCase> useCases = new HashSet<>();
            for (Long useCaseId : useCaseIds) {
                useCases.add(getUseCase(useCaseId));
            }
            card.setUseCases(useCases);
        }

        return crcCardRepo.save(card);
    }

    @Transactional
    public CRCCard updateCrcCard(Long cardId, String className, String responsibilities,
            String collaborations, List<Long> useCaseIds) {

        CRCCard card = getCrcCard(cardId);
        card.setClassName(className);
        card.setResponsibilities(responsibilities);
        card.setCollaborations(collaborations);

        Set<UseCase> useCases = new HashSet<>();
        if (useCaseIds != null && !useCaseIds.isEmpty()) {
            for (Long useCaseId : useCaseIds) {
                useCases.add(getUseCase(useCaseId));
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
