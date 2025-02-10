package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.infrastructure.repository.VideoProcessRepository;
import br.com.fiap.soat7.infrastructure.storage.UploadProcess;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UploadServiceTest {

    @Mock
    private VideoProcessRepository videoProcessRepository;

    @Mock
    private UploadProcess uploadProcess;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UploadService uploadService; // Let Spring inject the mocks

    @Test
    void testUploadFile_Sucesso() throws IOException {
        // Arrange: (Moved mock setup here)
        VideoProcess savedVideoProcess = new VideoProcess();
        savedVideoProcess.setId(123L);  // Simulate the ID being set by the database
        when(videoProcessRepository.save(any(VideoProcess.class))).thenReturn(savedVideoProcess);  // Return the object with the id

        when(uploadProcess.uploadFile(anyLong(), anyLong(), anyInt(), any(MultipartFile.class))).thenReturn("Upload realizado com sucesso");

        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("arquivo.txt");
        User user = new User();
        user.setId(1L);

        // Act
        String resultado = uploadService.uploadFile(file, user);

        // Assert
        assertEquals("Upload realizado com sucesso", resultado);
    }

    @Test
    public void testUploadFile_Erro() throws IOException {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        when(videoProcessRepository.save(any(VideoProcess.class))).thenThrow(new RuntimeException("Erro ao salvar vídeo"));

        // Configura o mock do UploadProcess
        UploadProcess uploadProcess = Mockito.mock(UploadProcess.class);

        // Configura o mock do RedisService
        RedisService redisService = Mockito.mock(RedisService.class);

        // Cria uma instância da classe UploadService
        UploadService uploadService = new UploadService(videoProcessRepository, uploadProcess, redisService);

        // Chama o método uploadFile
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("arquivo.txt");
        User user = new User();
        user.setId(1L);

        // Verifica se foi lançada uma exceção
        assertThrows(MissingFormatArgumentException.class, () -> uploadService.uploadFile(file, user));
    }


    @Test
    public void testGeNextVersionOrDefault() {
        // Configura o mock do VideoProcessRepository
        VideoProcessRepository videoProcessRepository = Mockito.mock(VideoProcessRepository.class);
        when(videoProcessRepository.findLastVersion(Mockito.anyString(), anyLong())).thenReturn(Optional.of(1));

        // Cria uma instância da classe UploadService
        UploadService uploadService = new UploadService(videoProcessRepository, Mockito.mock(UploadProcess.class), Mockito.mock(RedisService.class));

        // Chama o método geNextVersionOrDefault
        String videoName = "arquivo.txt";
        Long userId = 1L;
        int resultado = uploadService.geNextVersionOrDefault(videoName, userId);

        // Verifica se a próxima versão é 2
        assertEquals(2, resultado);
    }
}
