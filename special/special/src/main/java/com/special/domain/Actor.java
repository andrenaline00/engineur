package com.special.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false) // name is required
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // many actors can belong to one project
    @JoinColumn(name = "project_id", nullable = false) // foreign key to projects table, not null
    private Project project;

    @ManyToMany(mappedBy = "actors") // many-to-many relationship with UseCase, mapped by the "actors" field in UseCase
    private Set<UseCase> useCases = new HashSet<>();

    protected Actor() {
    } // default constructor for JPA

    public Actor(String name, String description, Project project) {
        this.name = name;
        this.description = description;
        this.project = project;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
    }

    public Set<UseCase> getUseCases() {
        return useCases;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setUseCases(Set<UseCase> useCases) {
        this.useCases = useCases;
    }

}
