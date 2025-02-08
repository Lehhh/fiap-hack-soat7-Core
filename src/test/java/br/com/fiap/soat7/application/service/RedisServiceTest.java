package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.configuration.ProcessamentoVideoAuthProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class RedisServiceTest {

    @Test
    public void testSendStatus_Sucesso() {
        // Configura o mock do RestTemplate
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ResponseEntity<String> response = ResponseEntity.ok("Status enviado com sucesso");
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(response);

        // Configura o mock do ProcessamentoVideoAuthProperties
        ProcessamentoVideoAuthProperties props = Mockito.mock(ProcessamentoVideoAuthProperties.class);
        Mockito.when(props.getRedisMidUrl()).thenReturn("http://localhost:8080");

        // Cria uma instância da classe RedisService
        RedisService redisService = new RedisService(restTemplate, props);

        // Chama o método sendStatus
        InfoVideo infoVideo = new InfoVideo();
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_FIX;
        Boolean resultado = redisService.sendStatus(infoVideo, statusRequest);

        // Verifica se o resultado foi true
        assertTrue(resultado);
    }

    @Test
    public void testSendStatus_Erro() {
        // Configura o mock do RestTemplate
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Configura o mock do ProcessamentoVideoAuthProperties
        ProcessamentoVideoAuthProperties props = Mockito.mock(ProcessamentoVideoAuthProperties.class);
        Mockito.when(props.getRedisMidUrl()).thenReturn("http://localhost:8080");

        // Cria uma instância da classe RedisService
        RedisService redisService = new RedisService(restTemplate, props);

        // Chama o método sendStatus
        InfoVideo infoVideo = new InfoVideo();
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_FIX;

        // Verifica se foi lançada uma exceção
        assertThrows(HttpServerErrorException.class, () -> redisService.sendStatus(infoVideo, statusRequest));
    }

    @Test
    public void testSendFix_Sucesso() {
        // Configura o mock do RestTemplate
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ResponseEntity<String> response = ResponseEntity.ok("Fix enviado com sucesso");
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(response);

        // Configura o mock do ProcessamentoVideoAuthProperties
        ProcessamentoVideoAuthProperties props = Mockito.mock(ProcessamentoVideoAuthProperties.class);
        Mockito.when(props.getRedisMidUrl()).thenReturn("http://localhost:8080");

        // Cria uma instância da classe RedisService
        RedisService redisService = new RedisService(restTemplate, props);

        // Chama o método sendFix
        String id = "123";
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_FIX;
        Boolean resultado = redisService.sendFix(id, statusRequest);

        // Verifica se o resultado foi true
        assertTrue(resultado);
    }

    @Test
    public void testSendFix_Erro() {
        // Configura o mock do RestTemplate
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Configura o mock do ProcessamentoVideoAuthProperties
        ProcessamentoVideoAuthProperties props = Mockito.mock(ProcessamentoVideoAuthProperties.class);
        Mockito.when(props.getRedisMidUrl()).thenReturn("http://localhost:8080");

        // Cria uma instância da classe RedisService
        RedisService redisService = new RedisService(restTemplate, props);

        // Chama o método sendFix
        String id = "123";
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_FIX;

        // Verifica se o resultado foi false
        assertFalse(redisService.sendFix(id, statusRequest));
    }
}
