package com.special.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.special.domain.CRCCard;



public interface CRCCardRepo extends JpaRepository<CRCCard, Long> {
	    List<CRCCard> findByProjectId(Long projectId);
	


}
