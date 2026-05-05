package com.special.repositories;


import com.special.domain.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UseCaseRepo extends JpaRepository<UseCase, Long> {
	
    List<UseCase> findByProjectID(Long projectID);

    void deleteByProjectID(Long projectID);
}











