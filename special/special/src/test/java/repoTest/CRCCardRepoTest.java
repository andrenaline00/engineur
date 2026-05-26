package repoTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.special.domain.CRCCard;
import com.special.domain.Project;
import com.special.domain.User;
import com.special.repositories.CRCCardRepo;


class CRCCardRepoTest {

	 @Autowired
	    private CRCCardRepo crcCardRepository;

	    @Autowired
	    private TestEntityManager entityManager;

	    @Test
	    void findByProjectId_returnsOnlyProjectCards() {
	        User user = entityManager.persist(new User("nabla", "pass","nabla@example.com","Onoma","admin"));
	        Project p1 = entityManager.persist(new Project("P1", "d1", user));
	        Project p2 = entityManager.persist(new Project("P2", "d2", user));
	        entityManager.persist(new CRCCard("UserService", p1));
	        entityManager.persist(new CRCCard("OrderService", p2));
	        entityManager.flush();

	        List<CRCCard> cards = crcCardRepository.findByProjectId(p1.getId());

	        assertEquals(1, cards.size());
	        assertEquals("UserService", cards.get(0).getClassName());
	    }
	

}
