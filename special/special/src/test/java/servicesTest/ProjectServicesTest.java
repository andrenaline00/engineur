package servicesTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.special.domain.Actor;
import com.special.domain.CRCCard;
import com.special.domain.Project;
import com.special.domain.UseCase;
import com.special.domain.User;
import com.special.repositories.ActorRepo;
import com.special.repositories.CRCCardRepo;
import com.special.repositories.ProjectRepo;
import com.special.repositories.UseCaseRepo;
import com.special.services.ProjectServices;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepo projectRepository;
    @Mock private ActorRepo actorRepository;
    @Mock private UseCaseRepo useCaseRepository;
    @Mock private CRCCardRepo crcCardRepository;

    @InjectMocks
    private ProjectServices projectService;

    private User user;
    private Project project;

    @BeforeEach
    void setUp() {
        user = new User("kleopatra", "pass" ,"kleopatra@example.com",true);
        user.setId(1L);
        project = new Project("TestProject", "desc", user);
        project.setId(1L);
    }

    // Project CRUD

    @Test
    void getProjectsByUser_returnsList() {
        when(projectRepository.findByUserId(1L)).thenReturn(List.of(project));
        List<Project> result = projectService.getProjectsForUser(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getProject_existing_returnsProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project result = projectService.getProject(1L);
        assertEquals("TestProject", result.getName());
    }

    @Test
    void getProject_nonExisting_throwsException() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> projectService.getProject(99L));
    }

    @Test
    void createProject_savesAndReturns() {
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));
        Project result = projectService.createProject("New", "desc", user.getId());
        assertEquals("New", result.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void deleteProject_callsRepository() {
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }

    // Actor CRUD

    @Test
    void createActor_savesWithProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(actorRepository.save(any(Actor.class))).thenAnswer(inv -> inv.getArgument(0));

        Actor result = projectService.createActor("Admin","description", 1L);
        assertEquals("Admin", result.getName());
        assertEquals(project, result.getProject());
    }

    @Test
    void updateActor_updatesName() {
        Actor actor = new Actor("OldName","description1", project);
        actor.setId(1L);
        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        when(actorRepository.save(any(Actor.class))).thenAnswer(inv -> inv.getArgument(0));

        Actor result = projectService.updateActor(1L, "NewName","description1");
        assertEquals("NewName", result.getName());
    }

    @Test
    void deleteActor_callsRepository() {
        projectService.deleteActor(1L);
        verify(actorRepository).deleteById(1L);
    }

    // UseCase CRUD

    @Test
    void createUseCase_withActors_savesAll() {
        Actor actor = new Actor("Admin","desc", project);
        actor.setId(10L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(actorRepository.findAllById(List.of(10L))).thenReturn(List.of(actor));
        when(useCaseRepository.save(any(UseCase.class))).thenAnswer(inv -> inv.getArgument(0));

        UseCase result = projectService.createNewUseCase("Login", "pre", "flow",
                "alt", "post", List.of(10L), 1L);

        assertEquals("Login", result.getTitle());
        assertEquals("pre", result.getPreconditions());
        assertEquals("flow", result.getMainFlow());
        assertEquals("alt", result.getAltFlows());
        assertEquals("post", result.getPostconditions());
        assertEquals(1, result.getActors().size());
    }

    @Test
    void createUseCase_withoutActors_savesWithoutActors() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(useCaseRepository.save(any(UseCase.class))).thenAnswer(inv -> inv.getArgument(0));

        UseCase result = projectService.createNewUseCase("Login", null, null,
                null, null, null, 1L);

        assertEquals("Login", result.getTitle());
        assertTrue(result.getActors().isEmpty());
    }

    @Test
    void updateUseCase_updatesAllFields() {
        UseCase useCase = new UseCase("Old", project);
        useCase.setId(1L);
        Actor actor = new Actor("Admin", "desc", project);
        actor.setId(10L);
        when(useCaseRepository.findById(1L)).thenReturn(Optional.of(useCase));
        when(actorRepository.findAllById(List.of(10L))).thenReturn(List.of(actor));
        when(useCaseRepository.save(any(UseCase.class))).thenAnswer(inv -> inv.getArgument(0));

        UseCase result = projectService.updateUseCase(1L, "New", "pre", "flow",
                "alt", "post", List.of(10L));

        assertEquals("New", result.getTitle());
        assertEquals(1, result.getActors().size());
    }

    @Test
    void deleteUseCase_callsRepository() {
        projectService.deleteUseCase(1L);
        verify(useCaseRepository).deleteById(1L);
    }

    // CRCCard CRUD

    @Test
    void createCrcCard_withUseCases_savesAll() {
        UseCase uc = new UseCase("Login", project);
        uc.setId(5L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(useCaseRepository.findAllById(List.of(5L))).thenReturn(List.of(uc));
        when(crcCardRepository.save(any(CRCCard.class))).thenAnswer(inv -> inv.getArgument(0));

        CRCCard result = projectService.createCrcCard("UserService", "handle login",
                "UserRepository", List.of(5L), 1L);

        assertEquals("UserService", result.getClassName());
        assertEquals("handle login", result.getResponsibilities());
        assertEquals("UserRepository", result.getCollaborations());
        assertEquals(1, result.getUseCases().size());
    }

    @Test
    void updateCrcCard_updatesAllFields() {
        CRCCard card = new CRCCard("OldClass", project);
        card.setId(1L);
        when(crcCardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(crcCardRepository.save(any(CRCCard.class))).thenAnswer(inv -> inv.getArgument(0));

        CRCCard result = projectService.updateCrcCard(1L, "NewClass", "resp", "collab", null);

        assertEquals("NewClass", result.getClassName());
        assertEquals("resp", result.getResponsibilities());
        assertTrue(result.getUseCases().isEmpty());
    }

    @Test
    void deleteCrcCard_callsRepository() {
        projectService.deleteCrcCard(1L);
        verify(crcCardRepository).deleteById(1L);
    }
}
