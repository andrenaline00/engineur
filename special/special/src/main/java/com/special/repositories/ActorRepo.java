package com.special.repositories;

import com.special.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepo extends JpaRepository<Actor,Long> {
    void deleteByProjectID(Long projectID);

    List<Actor> findByProjectID(Long projectID);
    //List<Actor> findByProjectID(Long projectID);
}
