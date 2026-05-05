package com.special.domain;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="useCases")
public class UseCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //da fak
    private Long ID;

    @Column(nullable = false) //whats nullable
    private String title;

    @Column(columnDefinition = "TEXT") //ti fash
    private String mainFlow;

    @Column(columnDefinition = "TEXT")
    private String altFlows;

    @Column(columnDefinition = "TEXT")
    private String postconditions;
    
    @Column(columnDefinition="TEXT")
    private String preconditions;

    @ManyToMany(fetch = FetchType.LAZY) //lazy is crazy wtf
    @JoinTable(name="useCaseActors",joinColumns = @JoinColumn(name="useCaseID"),inverseJoinColumns = @JoinColumn(name="actorID"))
    private Set<Actor> actors = new HashSet<>();
    
    @ManyToOne
    private Project project;

    public UseCase(String title,Project project){
        this.title=title;
        this.project=project;
    }

    //GETTERS
    public Long getID(){
        return ID;
    }

    public String getTitle(){
        return title;
    }

    
    public String getPreconditions(){
        return preconditions;
    }
    
    public String getMainFlow(){
        return mainFlow;
    }

    public String getAltFlows(){
        return altFlows;
    }

    public String getPostconditions(){
        return postconditions;
    }

    public Project getProject(){
        return project;
    }

    public Set<Actor> getActors(){
        return actors;
    }

    /*
    public Set<CRCCards> getCRCCards(){
        return CRCCard;
    }
    */


    //SETTERS
    public void setTitle(String title){
        this.title=title;
    }

    public void setPreconditions(String preconditions){
        this.preconditions=preconditions;
    }

    public void setMainFlow(String mainFlow){
        this.mainFlow=mainFlow;
    }

    public void setAltFlows(String altFlows){
        this.altFlows=altFlows;
    }

    public void setPostconditions(String postconditions){
        this.postconditions=postconditions;
    }

    public void setProject(Project project){
        this.project=project;
    }

    public void setActors(Set<Actor> actors){
        this.actors=actors;
    }

    
    public void addActor(Actor actor){
        this.actors.add(actor);
    }


}
