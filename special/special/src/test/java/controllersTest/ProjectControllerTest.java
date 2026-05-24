package controllersTest;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.DiagramService;
import com.special.services.ProjectServices;
import com.special.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock private ProjectServices projectService;
    @Mock private DiagramService diagramService;
    @Mock private UserService userService;

    private User mockUser() {
        User user = new User("Nabla","pass" ,"nablaki@zouzouni.com",true );
        user.setId(1L);
        return user;
    }

    @Test
    @WithMockUser(username = "andreana")
    void listProjects_returnsListView() throws Exception {
        when(userService.loadUserByUsername("andreana@example.com")).thenReturn(mockUser());
        when(projectService.getProjectsForUser(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/list"))
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    @WithMockUser(username = "xristiana")
    void createProject_redirectsToProjects() throws Exception {
        when(userService.loadUserByUsername("xristiana@example.com")).thenReturn(mockUser());
        when(projectService.createProject(anyString(), anyString(), any()))
                .thenReturn(new Project("P1", "d", mockUser()));

        mockMvc.perform(post("/projects")
                        .param("name", "NewProject")
                        .param("description", "desc")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    @WithMockUser(username = "theo")
    void newProjectForm_returnsCreateView() throws Exception {
        mockMvc.perform(get("/projects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/create"));
    }

    @Test
    @WithMockUser(username = "miaou@example.com")
    void viewProject_returnsViewPage() throws Exception {
        User user = new User("Miaou", "pass" ,"Maiou@example.com",true);
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        when(projectService.getProject(1L)).thenReturn(project);
        when(projectService.getUseCases(1L)).thenReturn(List.of());
        when(projectService.getActors(1L)).thenReturn(List.of());
        when(projectService.getCrcCards(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/view"))
                .andExpect(model().attributeExists("project", "useCases", "actors", "crcCards"));
    }

    @Test
    @WithMockUser(username = "alice@example.com")
    void deleteProject_redirectsToProjects() throws Exception {
        mockMvc.perform(post("/projects/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
        verify(projectService).deleteProject(1L);
    }

    @Test
    @WithMockUser(username = "alice@example.com")
    void useCaseDiagram_returnsScript() throws Exception {
        User user = new User("Alice", "pass","alice@example.com", true);
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        when(projectService.getProject(1L)).thenReturn(project);
        when(diagramService.generateUseCaseDiagram(1L, "plantuml")).thenReturn("@startuml\n@enduml");

        mockMvc.perform(get("/projects/1/diagrams/usecase").param("tool", "plantuml"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/diagram"))
                .andExpect(model().attribute("script", "@startuml\n@enduml"));
    }

    @Test
    @WithMockUser(username = "Nabla")
    void classDiagram_returnsScript() throws Exception {
        User user = new User("Nabla","pass" ,"nablaki@zouzouni.com",true);
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        when(projectService.getProject(1L)).thenReturn(project);
        when(diagramService.generateClassDiagram(1L, "nomnoml")).thenReturn("[ClassA]");

        mockMvc.perform(get("/projects/1/diagrams/class").param("tool", "nomnoml"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/diagram"))
                .andExpect(model().attribute("script", "[ClassA]"));
    }

    @Test
    void unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().is3xxRedirection());
    }
}
