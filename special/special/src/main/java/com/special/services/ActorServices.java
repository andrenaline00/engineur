package com.special.services;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.repositories.ActorRepo;
import com.special.repositories.UseCaseRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorServices {
    private final ActorRepo actorRepo;
    private final UseCaseRepo useCaseRepo;

    public ActorServices(ActorRepo actorRepo, UseCaseRepo useCaseRepo) {
        this.actorRepo = actorRepo;
        this.useCaseRepo = useCaseRepo;
    }

    public List<Actor> getActorsForProject(Long projectID) {
        return actorRepo.findByProjectID(projectID);
    }

    public Actor getActor(Long actorID) {
        return actorRepo.findById(actorID).orElseThrow(() -> new RuntimeException("Actor not found"));
    }

    @Transactional
    public Actor createActor(String name, String description, Project project){
        //or apla
        Actor myActor = new Actor(name, description, project);
        return actorRepo.save(myActor);
        //return actorRepo.save(new Actor(name, description, project));
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
        //remove associations with use cases
        actor.getUseCases().forEach(useCase -> useCase.getActors().remove(actor));
        useCaseRepo.saveAll(actor.getUseCases());
        actorRepo.delete(actor);
    }




}
