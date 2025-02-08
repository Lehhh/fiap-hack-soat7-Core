package br.com.fiap.soat7.infrastructure.repository;

import br.com.fiap.soat7.domain.ImageProcess;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.domain.enums.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional //  This is the key change!
class VideoProcessRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VideoProcessRepository videoProcessRepository;

    private User user;
    private VideoProcess videoProcess;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();


        videoProcess = new VideoProcess();
        videoProcess.setName("testVideo");
        videoProcess.setUserId(user);
        videoProcess.setVersion(1);
        entityManager.persist(videoProcess);
        entityManager.flush();

    }

    @Test
    void updateS3VideoStatus_ShouldUpdateStageSuccessfully() {
        // Arrange
        Long videoId = videoProcess.getId();
        Stage newStage = Stage.PROCESS_VIDEO_DONE;

        // Act
        int rowsAffected = videoProcessRepository.updateS3VideoStatus(videoId, newStage);
        VideoProcess updatedVideo = entityManager.find(VideoProcess.class, videoId);


        // Assert
        assertEquals(1, rowsAffected);
    }

    @Test
    void updateProcessVideoStatus_ShouldUpdateStageSuccessfully() {
        // Arrange
        Long videoId = videoProcess.getId();
        Stage newStage = Stage.PROCESS_VIDEO_DONE;

        // Act
        int rowsAffected = videoProcessRepository.updateProcessVideoStatus(videoId, newStage);
        VideoProcess updatedVideo = entityManager.find(VideoProcess.class, videoId);


        // Assert
        assertEquals(1, rowsAffected);
    }

    @Test
    void findLastVersion_ShouldReturnLatestVersionSuccessfully() {
        // Arrange
        String videoName = "testVideo";
        Long userId = user.getId();

        VideoProcess videoProcess2 = new VideoProcess();
        videoProcess2.setName("testVideo");
        videoProcess2.setUserId(user);
        videoProcess2.setVersion(2);
        entityManager.persist(videoProcess2);
        entityManager.flush();


        // Act
        Optional<Integer> lastVersion = videoProcessRepository.findLastVersion(videoName, userId);

        // Assert
        assertTrue(lastVersion.isPresent());
        assertEquals(2, lastVersion.get());
    }

    @Test
    void findLastVersion_ShouldReturnEmptyWhenNoVideoExists() {
        // Arrange
        String videoName = "nonExistingVideo";
        Long userId = user.getId();

        // Act
        Optional<Integer> lastVersion = videoProcessRepository.findLastVersion(videoName, userId);

        // Assert
        assertFalse(lastVersion.isPresent());
    }

    @Test
    void updateVersion_ShouldUpdateVersionSuccessfully() {
        // Arrange
        String videoName = "testVideo";
        Long userId = user.getId();
        Integer newVersion = 3;

        // Act
        videoProcessRepository.updateVersion(userId, videoName, newVersion);
        VideoProcess updatedVideo = videoProcessRepository.findByNameAndUserId(userId, videoName);

        // Assert
        assertNotNull(updatedVideo);
    }

    @Test
    void findByNameAndUserId_ShouldReturnVideoProcessSuccessfully() {
        // Arrange
        String videoName = "testVideo";
        Long userId = user.getId();

        // Act
        VideoProcess foundVideo = videoProcessRepository.findByNameAndUserId(userId, videoName);

        // Assert
        assertNotNull(foundVideo);
        assertEquals(videoName, foundVideo.getName());
        assertEquals(userId, foundVideo.getUserId().getId());
    }

    @Test
    void findByNameAndUserId_ShouldReturnNullWhenVideoNotFound() {
        // Arrange
        String videoName = "nonExistingVideo";
        Long userId = user.getId();

        // Act
        VideoProcess foundVideo = videoProcessRepository.findByNameAndUserId(userId, videoName);

        // Assert
        assertNull(foundVideo);
    }

    @Test
    void updateImageStatus_ShouldUpdateStageSuccessfully() {
        // Arrange
        ImageProcess imageProcess = new ImageProcess();
        imageProcess.setStage(Stage.PROCESS_VIDEO_DONE);
        entityManager.persist(imageProcess);
        entityManager.flush();


        Long imageId = imageProcess.getId();
        Stage newStage = Stage.PROCESS_VIDEO_DONE;

        // Act
        int rowsAffected = videoProcessRepository.updateImageStatus(imageId, newStage);
        ImageProcess updatedImage = entityManager.find(ImageProcess.class, imageId);

        // Assert
        assertEquals(1, rowsAffected);
        assertEquals(newStage, updatedImage.getStage());
    }
}