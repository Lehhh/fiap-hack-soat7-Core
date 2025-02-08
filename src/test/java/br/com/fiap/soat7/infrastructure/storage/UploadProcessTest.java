package br.com.fiap.soat7.infrastructure.storage;

import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadProcessTest {

    @InjectMocks
    private UploadProcess uploadProcess;

    private String uploadDir = "test-upload-dir/%d/%d/%d/";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(uploadProcess, "uploadDir", uploadDir);
    }

    @Test
    void uploadFile_emptyFile_returnsErrorMessage() throws IOException {
        // Arrange
        Long userId = 1L;
        Long videoId = 2L;
        Integer version = 3;
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        // Act
        String result = uploadProcess.uploadFile(userId, videoId, version, emptyFile);

        // Assert
        assertEquals(TextReponse.UPLOAD_DISK_ERROR_EMPTY, result);
    }

    @Test
    void uploadFile_successfulUpload_returnsSuccessMessage() throws IOException {
        // Arrange
        Long userId = 1L;
        Long videoId = 2L;
        Integer version = 3;
        String fileName = "test.txt";
        String content = "Test content";
        MultipartFile file = new MockMultipartFile("file", fileName, "text/plain", content.getBytes());
        String expectedPath = String.format(uploadDir, userId, videoId, version);

        // Create the directory before mocking to avoid actual file system interaction
        File directory = new File(expectedPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Act
        String result = uploadProcess.uploadFile(userId, videoId, version, file);

        // Assert
        assertEquals(String.format(TextReponse.SUCCESS_DISK_UPLOAD, fileName), result);

        // Clean up: Delete the test file and directory
        File uploadedFile = new File(expectedPath + fileName);
        assertTrue(uploadedFile.delete());
        assertTrue(directory.delete());

    }

    // Helper method to create directory (avoid duplication from test)
    private File createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}