package controllersTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.special.controllers.UserController;
import com.special.services.UserService;
import com.special.domain.User;


class AuthControllerTest {
	
	@InjectMocks
    private UserController authController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
	

	@Test
    void testLoginForm() throws Exception{
    	 mockMvc.perform(get("/login"))
         .andExpect(status().isOk())
         .andExpect(view().name("auth/login"));

    }
    
    @Test
    void testRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
               ;
    }
    
    @Test
    void testRegisteUser_alreadyRegistered() throws Exception{
    	when(userService.registerUser(anyString(), anyString(), anyString()))
        .thenReturn(new User("Teo","encoded", "teo@malimali.com","Onoma","admin"));

mockMvc.perform(post("/register")
                .param("username", "Teo").param("password", "pass123")
                .param("email", "teo@malimali.com")
                
                .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"))
        .andExpect(flash().attributeExists("success"));
    }
    
    @Test
    void profilePage_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    @WithMockUser(username = "gato@example.com")
    void profilePage_authenticated_returns200() throws Exception {
        User user = new User("Gato","encoded", "gato@example.com","Onoma","admin");
        user.setId(1L);
        when(userService.loadUserByUsername("gato@example.com")).thenReturn(user);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/profile"))
                .andExpect(model().attributeExists("user"));
    }
    
   

}
