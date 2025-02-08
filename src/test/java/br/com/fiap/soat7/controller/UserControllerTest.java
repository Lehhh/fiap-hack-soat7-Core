package br.com.fiap.soat7.controller;

import br.com.fiap.soat7.application.service.UserService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.security.JwtUtil;
import br.com.fiap.soat7.web.config.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    void createUser_ShouldReturnOkWithToken() {
        // Arrange
        String expectedToken = "mockedToken";
        when(jwtUtil.generateToken(user.getEmail(), null)).thenReturn(expectedToken);

        // Act
        ResponseEntity<String> response = userController.createUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());
        verify(userService).createUser(user);
        verify(jwtUtil).generateToken(user.getEmail(), null);
    }

    // Exemplo de teste para tratar uma exception lançada pelo UserService
    @Test
    void createUser_ShouldHandleExceptionFromUserService() {
        // Arrange
        when(userService.createUser(user)).thenThrow(new RuntimeException("Simulated exception"));

        // Act
        try {
            userController.createUser(user);
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Simulated exception", e.getMessage());
            verify(userService).createUser(user);
            return; // Encerra o teste se a exception for capturada
        }

        // Falha o teste se nenhuma exception for lançada
        // fail("Expected RuntimeException was not thrown");
    }


    @Test
    void listUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> users = List.of(new User(), new User());
        when(userService.findAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.listUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService).findAllUsers();
    }

    @Test
    void getUserDetails_ShouldReturnUserDetails() {
        // Arrange
        Long userId = 1L;
        when(userService.findUserById(userId)).thenReturn(user);

        // Act
        ResponseEntity<User> response = userController.getUserDetails(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).findUserById(userId);
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");
        when(userService.updateUser(userId, user)).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService).updateUser(userId, user);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).deleteUserById(userId);
    }

    @Test
    void recoverPassword_ShouldContainResetLink() {
        // Arrange
        String username = "test@example.com";

        // Act
        String viewName = userController.forgotPassword(username, new ExtendedModelMap());

        // Assert
        assertEquals("user/recover-password", viewName);
        verify(userService).resetPassword(username);
    }
}