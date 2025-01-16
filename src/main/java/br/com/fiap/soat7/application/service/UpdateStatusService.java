package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.infrastructure.repository.VideoProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStatusService {

	private final VideoProcessRepository videoProcessRepository;

	public Boolean updateStatusS3Video(Long videoId, Stage stage){
		return videoProcessRepository.updateS3VideoStatus(videoId, stage) == 1;
	}
	public Boolean updateStatusProcessVideo(Long videoId, Stage stage){
		return videoProcessRepository.updateProcessVideoStatus(videoId, stage) == 1;
	}
	public Boolean updateStatusS3Image(Long imageId, Stage stage){
		return videoProcessRepository.updateImageStatus(imageId, stage) == 1;
	}
}
