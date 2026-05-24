package controllersTest;

import com.special.controllers.UseCaseController;
import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.ProjectServices;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UseCaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectServices projectServices;

    @InjectMocks
    private UseCaseController useCaseController;
    
    private Project mockProject() {
        User user = new User("nabla", "pass","nabla@example.com","Onoma","admin" );
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        return project;
    }


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(useCaseController).build();
    }

    @Test
    void testNewUseCaseForm() throws Exception {
       
        when(projectServices.getProject(1L)).thenReturn(mockProject());
        when(projectServices.getActors(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/projects/1/usecases/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("usecases/create"))
                .andExpect(model().attributeExists("project", "actors"));
    }

    @Test
    void testCreateNewUseCaseRedirects() throws Exception {
        mockMvc.perform(post("/projects/1/usecases")
                        .param("title", "User Login")
                        .param("preconditions", "Main page open")
                        .param("mainFlow", "1. Enter credentials")
                        .param("altFlows", "1a. Wrong password")
                        .param("postconditions", "Dashboard open"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
                .andExpect(flash().attribute("success", "Use case created successfully"));

        verify(projectServices, times(1)).createNewUseCase(
                eq("User Login"), eq("Main page open"), eq("1. Enter credentials"),
                eq("1a. Wrong password"), eq("Dashboard open"), any(), eq(1L)
        );
    }

    @Test
    void testDeleteUseCaseRedirects() throws Exception {
        mockMvc.perform(post("/projects/1/usecases/10/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
               
                .andExpect(flash().attribute("Success", "Use Case Deleted Successfully"));

        verify(projectServices, times(1)).deleteUseCase(10L);
    }
}