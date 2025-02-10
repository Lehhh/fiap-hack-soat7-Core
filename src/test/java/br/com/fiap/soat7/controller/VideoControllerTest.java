package br.com.fiap.soat7.controller;

import br.com.fiap.soat7.application.service.UploadService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import br.com.fiap.soat7.web.controller.VideoController;
import br.com.fiap.soat7.web.restcontroller.UploadController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @Mock
    private UploadService uploadService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VideoController videoController;

    @InjectMocks
    private UploadController uploadController;

    private User user;
    private UserDetails userDetails;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Create a GrantedAuthority for the user. If no roles are defined, you can use an empty list.
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        userDetails = new org.springframework.security.core.userdetails.User("test@example.com", "password", authorities);

        file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "This is a test file".getBytes()
        );
    }

    @Test
    void handleFileUpload_ShouldReturnOkWithSuccessMessage() throws IOException {
        // Arrange
        String successMessage = TextReponse.SUCCESS + " File uploaded successfully.";
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(uploadService.uploadFile(file, user)).thenReturn(successMessage);

        // Act
        ResponseEntity<Map<String, String>> response = uploadController.handleFileUpload(file, userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(successMessage, response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void handleFileUpload_ShouldReturnInternalServerErrorWithErrorMessage() throws IOException {
        // Arrange
        String errorMessage = TextReponse.ERROR + " Failed to upload file.";
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(uploadService.uploadFile(file, user)).thenReturn(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = uploadController.handleFileUpload(file, userDetails);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void showUploadForm_ShouldReturnUploadViewName() {
        // Act
        String viewName = videoController.showUploadForm(null);

        // Assert
        assertEquals("upload", viewName);
    }

}