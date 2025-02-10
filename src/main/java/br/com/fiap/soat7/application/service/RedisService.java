package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.configuration.ProcessamentoVideoAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static br.com.fiap.soat7.infrastructure.configuration.TextReponse.ID;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {

	private final RestTemplate restTemplate;
	private final ProcessamentoVideoAuthProperties props;


	public Boolean sendStatus(InfoVideo infoVideo, StatusRequest statusRequest){
		try{
			ResponseEntity<String> exchange = restTemplate.exchange(props.getRedisMidUrl() + statusRequest.getEndPoint(),
					HttpMethod.POST,
					new HttpEntity<>(infoVideo),
					String.class);
			log.info(exchange.getBody());
			return exchange.getStatusCode().is2xxSuccessful();
		}
		catch (Exception e){
			log.error(e.getMessage());
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public Boolean sendFix(String id, StatusRequest statusRequest){
		try{
			ResponseEntity<String> exchange = restTemplate.exchange(props.getRedisMidUrl() + statusRequest.getEndPoint(),
					HttpMethod.DELETE,
					new HttpEntity<>(Map.of(ID, id)),
					String.class);
			return exchange.getStatusCode().is2xxSuccessful();
		}
		catch (Exception e){
			log.error(e.getMessage());
			return false;
		}
	}
}
