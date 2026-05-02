package com.special.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.special.domain.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findById(Long Id);

}
