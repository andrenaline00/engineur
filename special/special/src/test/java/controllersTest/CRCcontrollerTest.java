package controllersTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.special.domain.CRCCard;
import com.special.domain.Project;
import com.special.domain.User;
import com.special.services.ProjectServices;
import com.special.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class CRCControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ProjectServices projectService;
    @MockitoBean private UserService userService;

    private Project mockProject() {
        User user = new User("GG","pass", "GG@example.com",true);
        Project project = new Project("P1", "desc", user);
        project.setId(1L);
        return project;
    }

    @Test
    @WithMockUser
    void newCrcCardForm_returnsCreateView() throws Exception {
        when(projectService.getProject(1L)).thenReturn(mockProject());
        when(projectService.getUseCases(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1/crccards/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("crccards/create"))
                .andExpect(model().attributeExists("project", "useCases"));
    }

    @Test
    @WithMockUser
    void createCrcCard_redirectsToProject() throws Exception {
        when(projectService.createCrcCard(anyString(), any(), any(), any(), eq(1L)))
                .thenReturn(new CRCCard("UserService", mockProject()));

        mockMvc.perform(post("/projects/1/crccards")
                        .param("className", "UserService")
                        .param("responsibilities", "Handle login")
                        .param("collaborations", "UserRepository")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void editCrcCardForm_returnsEditView() throws Exception {
        CRCCard card = new CRCCard("UserService", mockProject());
        card.setId(3L);
        when(projectService.getProject(1L)).thenReturn(mockProject());
        when(projectService.getCrcCard(3L)).thenReturn(card);
        when(projectService.getUseCases(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1/crccards/3/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("crccards/edit"))
                .andExpect(model().attributeExists("crcCard"));
    }

    @Test
    @WithMockUser
    void updateCrcCard_redirectsToProject() throws Exception {
        when(projectService.updateCrcCard(eq(3L), anyString(), any(), any(), any()))
                .thenReturn(new CRCCard("Updated", mockProject()));

        mockMvc.perform(post("/projects/1/crccards/3")
                        .param("className", "Updated")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void deleteCrcCard_redirectsToProject() throws Exception {
        mockMvc.perform(post("/projects/1/crccards/3/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
        verify(projectService).deleteCrcCard(3L);
    }
}
