package com.special.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "crc_cards")
public class CRCCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "class_name", unique = true)
	private String className;

	@Column(name = "responsibilities")
	private String responsibilities;

	@Column(name = "collaborations")
	private String collaborations;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@ManyToMany
	@JoinTable(name = "crc_card_use_cases", joinColumns = @JoinColumn(name = "crc_card_id"), inverseJoinColumns = @JoinColumn(name = "use_case_id"))
	private Set<UseCase> useCases = new HashSet<>();

	public CRCCard(String className, Project project) {
		this.className = className;
		this.project = project;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setClassname(String className) {
		this.className = className;
	}

	public String getclassname() {
		return className;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	public String getCollaborations() {
		return collaborations;
	}

	public void setCollaborations(String collaborations) {
		this.collaborations = collaborations;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<UseCase> getUseCases() {
		return useCases;
	}

	public void setUseCases(Set<UseCase> useCases) {
		this.useCases = useCases;
	}
}
