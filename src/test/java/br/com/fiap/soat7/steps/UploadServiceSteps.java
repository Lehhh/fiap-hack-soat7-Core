package br.com.fiap.soat7.steps;

import br.com.fiap.soat7.application.service.UploadService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.repository.VideoProcessRepository;
import br.com.fiap.soat7.infrastructure.storage.UploadProcess;
import br.com.fiap.soat7.application.service.RedisService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UploadServiceSteps {

    @InjectMocks
    private UploadService uploadService;

    @Mock
    private VideoProcessRepository videoProcessRepository;

    @Mock
    private UploadProcess uploadProcess;

    @Mock
    private RedisService redisService;

    private User user;
    private MultipartFile file;
    private String uploadResponse;
    private IOException ioException;
    private int existingVersion = 1;
    private VideoProcess savedVideoProcess;

    @Captor
    private ArgumentCaptor<InfoVideo> infoVideoCaptor;
    @Captor
    private ArgumentCaptor<StatusRequest> statusRequestCaptor;

    public UploadServiceSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("um usuário com ID {string} e nome {string}")
    public void umUsuárioComIdENome(String id, String nome) {
        user = new User();
        user.setId(Long.parseLong(id));
        user.setEmail(nome);
    }

    @Given("um arquivo de vídeo chamado {string}")
    public void umArquivoDeVídeoChamado(String filename) {
        file = new MockMultipartFile(
                "file",
                filename,
                "video/mp4",
                "Conteúdo do vídeo".getBytes()
        );
    }

    @Given("um arquivo de vídeo chamado {string} já existe com a versão {string}")
    public void umArquivoDeVídeoChamadoJáExisteComAVersão(String filename, String version) {
        umArquivoDeVídeoChamado(filename);
        existingVersion = Integer.parseInt(version);

        VideoProcess existingVideo = new VideoProcess();
        existingVideo.setId(1L);
        existingVideo.setName(filename);
        existingVideo.setVersion(existingVersion);

        when(videoProcessRepository.findLastVersion(filename, user.getId()))
                .thenReturn(Optional.of(existingVersion));

        when(videoProcessRepository.findByNameAndUserId(user.getId(), filename)).thenReturn(existingVideo);
    }

    @When("o usuário faz upload do arquivo")
    public void oUsuárioFazUploadDoArquivo() throws IOException {
        when(uploadProcess.uploadFile(anyLong(), anyLong(), anyInt(), any(MultipartFile.class))).thenReturn("upload-success");
        when(videoProcessRepository.save(any(VideoProcess.class))).thenAnswer(invocation -> {
            savedVideoProcess = invocation.getArgument(0);
            savedVideoProcess.setId(1L); // Simula o ID sendo gerado pelo banco
            return savedVideoProcess;
        });

        try {
            uploadResponse = uploadService.uploadFile(file, user);
        } catch (IOException e) {
            ioException = e;
        }
    }

    @Then("o upload é realizado com sucesso")
    public void oUploadÉRealizadoComSucesso() {
        Assertions.assertEquals("upload-success", uploadResponse);
    }

    @Then("uma nova entrada de VideoProcess é criada no banco de dados")
    public void umaNovaEntradaDeVideoProcessÉCriadaNoBancoDeDados() {
        Mockito.verify(videoProcessRepository, times(1)).save(any(VideoProcess.class));
        Assertions.assertEquals(1, savedVideoProcess.getVersion());

        verify(redisService).sendStatus(infoVideoCaptor.capture(), statusRequestCaptor.capture());

        InfoVideo capturedInfoVideo = infoVideoCaptor.getValue();
        StatusRequest capturedStatusRequest = statusRequestCaptor.getValue();

        Assertions.assertEquals(Stage.UPLOAD_DISK_DONE, capturedInfoVideo.getStage());
        Assertions.assertEquals(StatusRequest.DISK_STATUS, capturedStatusRequest);

    }

    @Then("a versão do VideoProcess é atualizada no banco de dados para {string}")
    public void aVersãoDoVideoProcessÉAtualizadaNoBancoDeDadosPara(String novaVersao) {
        int expectedVersion = Integer.parseInt(novaVersao);
        verify(videoProcessRepository).updateVersion(user.getId(), file.getOriginalFilename(), expectedVersion);

        verify(redisService).sendStatus(infoVideoCaptor.capture(), statusRequestCaptor.capture());

        InfoVideo capturedInfoVideo = infoVideoCaptor.getValue();
        StatusRequest capturedStatusRequest = statusRequestCaptor.getValue();

        Assertions.assertEquals(Stage.UPLOAD_DISK_DONE, capturedInfoVideo.getStage());
        Assertions.assertEquals(StatusRequest.DISK_STATUS, capturedStatusRequest);
    }

    @When("o upload falha devido a um erro de IO")
    public void oUploadFalhaDevidoAUmErroDeIO() throws IOException {
        when(uploadProcess.uploadFile(anyLong(), anyLong(), anyInt(), any(MultipartFile.class)))
                .thenThrow(new IOException("Simulated IO Error"));

        try {
            uploadService.uploadFile(file, user);
        } catch (IOException e) {
            ioException = e;
        } catch (Exception e) {
            ioException = new IOException("Erro inesperado: " + e.getMessage());
        }
    }

    @Then("uma exceção de IO é lançada")
    public void umaExceçãoDeIOÉLançada() {
        Assertions.assertNotNull(ioException);
    }
}
