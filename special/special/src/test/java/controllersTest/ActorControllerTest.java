package controllersTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.special.domain.Actor;
import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.ProjectServices;
import com.special.services.UserService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ProjectServices projectService;
    @MockitoBean private UserService userService;

    private Project mockProject() {
        User user = new User("Px", "pass" ,"px@example.com","Onoma","admin");
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        return project;
    }

    @Test
    @WithMockUser
    void newActorForm_returnsCreateView() throws Exception {
        when(projectService.getProject(1L)).thenReturn(mockProject());

        mockMvc.perform(get("/projects/1/actors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("actors/create"));
    }

    @Test
    @WithMockUser
    void createActor_redirectsToProject() throws Exception {
        when(projectService.createActor("gatoulis","xontroulhsgatoulhs", 1L))
                .thenReturn(new Actor("gatoulis","xontroulhsgatoulhs", mockProject()));

        mockMvc.perform(post("/projects/1/actors")
                        .param("name", "Admin")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void editActorForm_returnsEditView() throws Exception {
        Actor actor = new Actor("Admin", "pame polu", mockProject());
        actor.setId(2L);
        when(projectService.getProject(1L)).thenReturn(mockProject());
        when(projectService.getActor(2L)).thenReturn(actor);

        mockMvc.perform(get("/projects/1/actors/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("actors/edit"))
                .andExpect(model().attributeExists("actor"));
    }

    @Test
    @WithMockUser
    void updateActor_redirectsToProject() throws Exception {
        when(projectService.updateActor(2L,"niaou", "Updated"))
                .thenReturn(new Actor("niaou", "Updated", mockProject()));

        mockMvc.perform(post("/projects/1/actors/2")
                        .param("name", "Updated")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void deleteActor_redirectsToProject() throws Exception {
        mockMvc.perform(post("/projects/1/actors/2/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
        verify(projectService).deleteActor(2L);
    }
}
