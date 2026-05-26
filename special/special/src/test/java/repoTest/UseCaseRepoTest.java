package repoTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.special.domain.Project;
import com.special.domain.UseCase;
import com.special.domain.User;
import com.special.repositories.UseCaseRepo;


class UseCaseRepoTest {

	  @Autowired
	    private UseCaseRepo useCaseRepository;

	    @Autowired
	    private TestEntityManager entityManager;

	    @Test
	    void findByProjectId_returnsOnlyProjectUseCases() {
	        User user = entityManager.persist(new User("nabla", "pass","nabla@example.com","Onoma","admin"));
	        Project p1 = entityManager.persist(new Project("P1", "d1", user));
	        Project p2 = entityManager.persist(new Project("P2", "d2", user));
	        entityManager.persist(new UseCase("Login", p1));
	        entityManager.persist(new UseCase("Register", p1));
	        entityManager.persist(new UseCase("Checkout", p2));
	        entityManager.flush();

	        List<UseCase> useCases = useCaseRepository.findByProjectId(p1.getId());

	        assertEquals(2, useCases.size());
	        assertTrue(useCases.stream().allMatch(uc -> uc.getProject().getId().equals(p1.getId())));
	    }
	

}
