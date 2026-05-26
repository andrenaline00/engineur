package repoTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.User;
import com.special.repositories.ActorRepo;


class ActorRepoTest {

	   @Autowired
	    private ActorRepo actorRepository;

	    @Autowired
	    private TestEntityManager entityManager;

	    @Test
	    void findByProjectId_returnsOnlyProjectActors() {
	        User user = entityManager.persist(new User("nabla", "pass","nabla@example.com","Onoma","admin"));
	        Project p1 = entityManager.persist(new Project("P1", "d1", user));
	        Project p2 = entityManager.persist(new Project("P2", "d2", user));
	        entityManager.persist(new Actor("Admin","admin", p1));
	        entityManager.persist(new Actor("User","User", p1));
	        entityManager.persist(new Actor("Guest","User", p2));
	        entityManager.flush();

	        List<Actor> actors = actorRepository.findByProjectId(p1.getId());

	        assertEquals(2, actors.size());
	        assertTrue(actors.stream().allMatch(a -> a.getProject().getId().equals(p1.getId())));
	    }
}
