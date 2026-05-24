package com.special.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrementing primary key
    private Long id;

    @Column(name = "name", nullable = false) // name is required
    private String name;

    @Column(name = "description", nullable = false) // description is required
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) //fetch user lazily to avoid loading user data unless needed
    @JoinColumn(name = "user_id", nullable = false) //nullable = false because every project must belong to a user
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true) // one project can have many actors, cascade all operations, remove orphan actors when removed from project
    private List<Actor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UseCase> useCases = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CRCCard> crcCards = new ArrayList<>();

    // another one for diagrams perhaps

    public Project(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    protected Project() {

    }

    // GETTERS
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<UseCase> getUseCases() {
        return useCases;
    }

    public List<CRCCard> getCRCCards() {
        return crcCards;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUseCases(List<UseCase> useCases) {
        this.useCases = useCases;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void setCRCCards(List<CRCCard> crcCards) {
        this.crcCards = crcCards;
    }

	public void setId(long l) {
		this.id=l;
		
	}

}
