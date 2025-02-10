package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.Role;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.repository.RoleRepository;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User("user@example.com", "password", new Role("USER"));
        when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Simulate the userRepository.save() method returning the saved user
        User savedUser = new User("user@example.com", "encodedPassword", new Role("USER"));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        verify(userRepository, Mockito.times(1)).save(any(User.class));
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    public void testCreateAdminIfNotExist() {
        // Configura o mock do UserRepository
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // Configura o mock do RoleRepository
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        when(roleRepository.findAll()).thenReturn(List.of(new Role("USER")));

        // Configura o mock do BCryptPasswordEncoder
        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Cria uma instância da classe UserService
        UserService userService = new UserService(userRepository, roleRepository, bCryptPasswordEncoder);

        // Chama o método createAdminIfNotExist
        userService.createAdminIfNotExist();

        // Verifica se o admin foi criado
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }


    @Test
    public void testCreateUserWithError() {
        // Configura o mock do UserRepository
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Erro ao criar usuário"));

        // Configura o mock do BCryptPasswordEncoder
        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Cria uma instância da classe UserService
        UserService userService = new UserService(userRepository, Mockito.mock(RoleRepository.class), bCryptPasswordEncoder);

        // Cria um usuário
        User user = new User("user@example.com", "password", new Role("USER"));

        // Chama o método createUser
        try {
            userService.createUser(user);
            fail("Deveria ter lançado uma exceção");
        } catch (RuntimeException e) {
            assertEquals("Erro ao criar usuário", e.getMessage());
        }
    }

    @Test
    void testResetPassword_UserExists() {
        // Arrange
        String userEmail = "existing_user@example.com";
        User existingUser = new User(userEmail, "oldPassword", new Role("USER"));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.encode(existingUser.getPassword())).thenReturn("encodedPassword");
        Role userRole = new Role("USER");
        when(roleRepository.findByName("USER")).thenReturn(userRole);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.resetPassword(userEmail);

        // Assert
        assertEquals(existingUser, result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(userRole, result.getRole());
        verify(userRepository).findByEmail(userEmail);
        verify(roleRepository).findByName("USER");
        verify(userRepository).save(existingUser);
    }

    @Test
    void testResetPassword_UserDoesNotExist() {
        // Arrange
        String userEmail = "new_user@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(UserService.DEFAULT_PASSWORD)).thenReturn("encodedPassword");
        Role userRole = new Role("USER");
        when(roleRepository.findByName("USER")).thenReturn(userRole);
        User newUser = new User(userEmail, "encodedPassword", userRole);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.resetPassword(userEmail);

        // Assert
        assertEquals(newUser, result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(userRole, result.getRole());
        verify(userRepository).findByEmail(userEmail);
        verify(bCryptPasswordEncoder).encode(UserService.DEFAULT_PASSWORD);
        verify(roleRepository).findByName("USER");
        verify(userRepository).save(any(User.class));
    }

}
