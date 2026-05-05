package com.special.repositories;

import com.special.domain.Project;

import java.util.List;

public interface ProjectRepo {
    List<Project> findByUserID(Long userID);

    List<Project> findById(Long projectID);

    Project save(Project project);

    void deleteById(Long projectID);
}
