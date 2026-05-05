package com.special.repositories;

import com.special.domain.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UseCaseRepo {
    List<UseCase> findByProjectID(Long projectID);
}











}
