package br.com.fiap.soat7.controller;

import br.com.fiap.soat7.application.service.UpdateStatusService;
import br.com.fiap.soat7.domain.dto.StatusRequestUpdate;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import br.com.fiap.soat7.web.restcontroller.UpdateStatusController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateStatusControllerTest {

    @InjectMocks
    private UpdateStatusController updateStatusController;

    @Mock
    private UpdateStatusService updateStatusService;

    private StatusRequestUpdate statusRequestUpdate;

    @BeforeEach
    void setUp() {
        statusRequestUpdate = new StatusRequestUpdate();
        statusRequestUpdate.setStage(Stage.PROCESS_VIDEO_DONE);
    }

    @Test
    void updateStatusUploadS3Video_Success() {
        // Arrange
        Long videoId = 1L;
        when(updateStatusService.updateStatusS3Video(videoId, statusRequestUpdate.getStage())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusUploadS3Video(videoId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void updateStatusUploadS3Video_Failure() {
        // Arrange
        Long videoId = 1L;
        when(updateStatusService.updateStatusS3Video(videoId, statusRequestUpdate.getStage())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusUploadS3Video(videoId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void updateStatusProcessVideo_Success() {
        // Arrange
        Long videoId = 1L;
        when(updateStatusService.updateStatusProcessVideo(videoId, statusRequestUpdate.getStage())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusProcessVideo(videoId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void updateStatusProcessVideo_Failure() {
        // Arrange
        Long videoId = 1L;
        when(updateStatusService.updateStatusProcessVideo(videoId, statusRequestUpdate.getStage())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusProcessVideo(videoId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void updateStatusUploadS3Images_Success() {
        // Arrange
        Long imageId = 1L;
        when(updateStatusService.updateStatusS3Image(imageId, statusRequestUpdate.getStage())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusUploadS3Images(imageId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get(TextReponse.MESSAGE));
    }

    @Test
    void updateStatusUploadS3Images_Failure() {
        // Arrange
        Long imageId = 1L;
        when(updateStatusService.updateStatusS3Image(imageId, statusRequestUpdate.getStage())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Boolean>> response = updateStatusController.updateStatusUploadS3Images(imageId, statusRequestUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().get(TextReponse.MESSAGE));
    }
}