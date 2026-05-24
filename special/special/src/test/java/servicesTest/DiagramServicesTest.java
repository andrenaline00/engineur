package servicesTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.special.diagrams.ClassDiagramFactory;
import com.special.diagrams.UseCaseFactory;
import com.special.domain.Actor;
import com.special.domain.CRCCard;
import com.special.domain.Project;
import com.special.domain.UseCase;
import com.special.domain.User;
import com.special.services.DiagramService;
import com.special.services.ProjectServices;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DiagramServiceTest {

    @Mock
    private ProjectServices projectService;

    private DiagramService diagramService;
    private Project project;

    @BeforeEach
    void setUp() {
        diagramService = new DiagramService(
                new UseCaseFactory(),
                new ClassDiagramFactory(),
                projectService
        );

        User user = new User("test", "pass" ,"test@example.com","Onoma","admin");
        project = new Project("TestProject", "desc", user);
        project.setId(1L);

        Actor actor = new Actor("Admin","eimai actor", project);
        project.setActors(List.of(actor));

        UseCase uc = new UseCase("Login", project);
        uc.getActors().add(actor);
        project.setUseCases(List.of(uc));

        CRCCard card = new CRCCard("UserService", project);
        card.setResponsibilities("Handle authentication");
        card.setCollaborations("UserRepository");
        project.setCRCCards(List.of(card));
    }

    @Test
    void generateUseCaseDiagram_plantuml_returnsScript() {
        when(projectService.getProject(1L)).thenReturn(project);
        String result = diagramService.generateUseCaseDiagram(1L, "plantuml");
        assertTrue(result.contains("@startuml"));
        assertTrue(result.contains("@enduml"));
        assertTrue(result.contains("Admin"));
        assertTrue(result.contains("Login"));
    }

    @Test
    void generateUseCaseDiagram_nomnoml_returnsScript() {
        when(projectService.getProject(1L)).thenReturn(project);
        String result = diagramService.generateUseCaseDiagram(1L, "nomnoml");
        assertTrue(result.contains("<actor>"));
        assertTrue(result.contains("Admin"));
        assertTrue(result.contains("Login"));
    }

    @Test
    void generateClassDiagram_plantuml_returnsScript() {
        when(projectService.getProject(1L)).thenReturn(project);
        String result = diagramService.generateClassDiagram(1L, "plantuml");
        assertTrue(result.contains("@startuml"));
        assertTrue(result.contains("class UserService"));
        assertTrue(result.contains("UserRepository"));
    }

    @Test
    void generateClassDiagram_nomnoml_returnsScript() {
        when(projectService.getProject(1L)).thenReturn(project);
        String result = diagramService.generateClassDiagram(1L, "nomnoml");
        assertTrue(result.contains("UserService"));
        assertTrue(result.contains("UserRepository"));
    }

    @Test
    void generateDiagram_unsupportedTool_throwsException() {
        when(projectService.getProject(1L)).thenReturn(project);
        assertThrows(IllegalArgumentException.class,
                () -> diagramService.generateUseCaseDiagram(1L, "mermaid"));
    }
}
