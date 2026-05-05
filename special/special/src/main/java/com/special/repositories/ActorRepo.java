package com.special.repositories;

import com.special.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepo extends JpaRepository<Actor,Long> {
    void deleteByProjectID(Long projectID);

    List<Actor> findByProjectID(Long projectID);
    //List<Actor> findByProjectID(Long projectID);
}
