package repoTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.special.domain.User;
import com.special.repositories.UserRepository;



class UserRepoTest {

	 @Autowired
	    private UserRepository userRepo;

	    @Test
	    void findByEmail_existingUser_returnsUser() {
	        User user = new User("nabla", "pass","nabla@example.com","Nablito","admin");
	        userRepo.save(user);

	        Optional<User> found = userRepo.findByEmail("nabla@example.com");

	        assertTrue(found.isPresent());
	        assertEquals("nabla", found.get().getUsername());
	    }

	    @Test
	    void findByEmail_nonExistingUser_returnsEmpty() {
	        Optional<User> found = userRepo.findByEmail("nobody@example.com");
	        assertTrue(found.isEmpty());
	    }
}
