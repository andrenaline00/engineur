package repoTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.special.domain.Project;
import com.special.domain.User;
import com.special.repositories.ProjectRepo;



class ProjectRepoTest {

	 @Autowired
	    private ProjectRepo projectRepository;

	    @Autowired
	    private TestEntityManager entityManager;

	    @Test
	    void findByUserId_returnsOnlyUserProjects() {
	        User user1 = entityManager.persist(new User("nabla", "pass","nabla@example.com","Onoma","admin"));
	        User user2 = entityManager.persist(new User("tsitsi", "pass","tsitsi@example.com","Onoma","admin"));
	        entityManager.persist(new Project("P1", "desc1", user1));
	        entityManager.persist(new Project("P2", "desc2", user1));
	        entityManager.persist(new Project("P3", "desc3", user2));
	        entityManager.flush();

	        List<Project> projects = projectRepository.findByUserId(user1.getId());

	        assertEquals(2, projects.size());
	        assertTrue(projects.stream().allMatch(p -> p.getId().equals(user1.getId())));
	    }

}
