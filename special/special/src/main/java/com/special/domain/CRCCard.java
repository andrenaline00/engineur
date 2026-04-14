package com.special.domain;

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
