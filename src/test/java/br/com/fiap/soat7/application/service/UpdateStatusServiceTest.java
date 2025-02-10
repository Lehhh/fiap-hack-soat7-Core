package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.infrastructure.repository.VideoProcessRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStatusServiceTest {
    @Test
    public void testUpdateStatusS3Video_Sucesso() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        Mockito.when(videoProcessRepository.updateS3VideoStatus(Mockito.anyLong(), Mockito.any(Stage.class))).thenReturn(1);

        // Cria uma instância da classe UpdateStatusService
        UpdateStatusService updateStatusService = new UpdateStatusService(videoProcessRepository);

        // Chama o método updateStatusS3Video
        Long videoId = 123L;
        Stage stage = Stage.PROCESS_VIDEO_DONE;
        Boolean resultado = updateStatusService.updateStatusS3Video(videoId, stage);

        // Verifica se o resultado foi true
        assertTrue(resultado);
    }

    @Test
    public void testUpdateStatusS3Video_Erro() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        Mockito.when(videoProcessRepository.updateS3VideoStatus(Mockito.anyLong(), Mockito.any(Stage.class))).thenReturn(0);

        // Cria uma instância da classe UpdateStatusService
        UpdateStatusService updateStatusService = new UpdateStatusService(videoProcessRepository);

        // Chama o método updateStatusS3Video
        Long videoId = 123L;
        Stage stage = Stage.UPLOAD_S3_IMAGES_ERROR;
        Boolean resultado = updateStatusService.updateStatusS3Video(videoId, stage);

        // Verifica se o resultado foi false
        assertFalse(resultado);
    }

    @Test
    public void testUpdateStatusProcessVideo_Sucesso() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        Mockito.when(videoProcessRepository.updateProcessVideoStatus(Mockito.anyLong(), Mockito.any(Stage.class))).thenReturn(1);

        // Cria uma instância da classe UpdateStatusService
        UpdateStatusService updateStatusService = new UpdateStatusService(videoProcessRepository);

        // Chama o método updateStatusProcessVideo
        Long videoId = 123L;
        Stage stage = Stage.PROCESS_VIDEO_DONE;
        Boolean resultado = updateStatusService.updateStatusProcessVideo(videoId, stage);

        // Verifica se o resultado foi true
        assertTrue(resultado);
    }

    @Test
    public void testUpdateStatusProcessVideo_Erro() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        Mockito.when(videoProcessRepository.updateProcessVideoStatus(Mockito.anyLong(), Mockito.any(Stage.class))).thenReturn(0);

        // Cria uma instância da classe UpdateStatusService
        UpdateStatusService updateStatusService = new UpdateStatusService(videoProcessRepository);

        // Chama o método updateStatusProcessVideo
        Long videoId = 123L;
        Stage stage = Stage.UPLOAD_S3_IMAGES_ERROR;
        Boolean resultado = updateStatusService.updateStatusProcessVideo(videoId, stage);

        // Verifica se o resultado foi false
        assertFalse(resultado);
    }

    @Test
    public void testUpdateStatusS3Image_Sucesso() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        Mockito.when(videoProcessRepository.updateImageStatus(Mockito.anyLong(), Mockito.any(Stage.class))).thenReturn(1);

        // Cria uma instância da classe UpdateStatusService
        UpdateStatusService updateStatusService = new UpdateStatusService(videoProcessRepository);

        // Chama o método updateStatusS3Image
        Long imageId = 123L;
        Stage stage = Stage.UPLOAD_DISK_DONE;
        Boolean resultado = updateStatusService.updateStatusS3Image(imageId, stage);

        // Verifica se o resultado foi true
        assertTrue(resultado);
    }


}
