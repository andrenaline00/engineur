package servicesTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.special.domain.User;
import com.special.repositories.UserRepository;
import com.special.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("aitheras", "pass" ,"aitheras@example.com","Onoma","admin");
        testUser.setId(1L);
    }

    @Test
    void loadUserByUsername_existingUser_returnsUserDetails() {
        when(userRepository.findByEmail("aitheras@example.com")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("aitheras");

        assertEquals("aitheras", result.getUsername());
    }

    @Test
    void loadUserByUsername_nonExistingUser_throwsException() {
        when(userRepository.findByEmail("piripiri@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("piripiri@example.com"));
    }

    @Test
    void register_newUser_savesAndReturnsUser() {
        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.registerUser("tsirli", "tsirli@example.com", "rawPass");

        assertEquals("tsirli", result.getUsername());
        assertEquals("tsirli@example.com", result.getEmail());
        assertEquals("encodedPass", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_existingEmail_throwsException() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser("Alice", "alice@example.com", "pass"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_existingUser_updatesFields() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPass")).thenReturn("newEncodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateProfile(1L, "Alice Updated", "alice2@example.com", "newPass");

        assertEquals("Alice Updated", result.getUsername());
        assertEquals("alice2@example.com", result.getEmail());
        assertEquals("newEncodedPass", result.getPassword());
    }

    @Test
    void updateProfile_blankPassword_keepsOldPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateProfile(1L, "Alice", "alice@example.com", "");

        assertEquals("encodedPass", result.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void findById_existingUser_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User result = userService.getUserById(1L);
        assertEquals("Alice", result.getUsername());
    }

    @Test
    void findById_nonExistingUser_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(99L));
    }
}
