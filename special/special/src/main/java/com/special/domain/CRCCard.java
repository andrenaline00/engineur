package com.special.domain;

import java.util.HashSet;
import java.util.Set;

import com.uoi.reqanalysis.domain.Project;
import com.uoi.reqanalysis.domain.UseCase;

import jakarta.persistence.*;

@Entity
@Table(name="crc_cards")
public class CRCCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="class_name",unique=true)
	private String className;
	
	@Column(name="responsibilities")
	private String responsibilities;
	
	@Column(name = "collaborations")
	private String collaborations;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
	
	private Set<UseCase> useCases = new HashSet<>();
	
	public CRCCard(String className, Project project) {
        this.className = className;
        this.project = project;
    }
	
	public String getclassname() {
		return className;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setclassname(String className) {
		this.className = className;
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

	
}
