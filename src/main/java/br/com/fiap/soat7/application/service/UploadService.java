package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.repository.VideoProcessRepository;
import br.com.fiap.soat7.infrastructure.storage.UploadProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadService {

	private final VideoProcessRepository videoProcessRepository;
	private final UploadProcess uploadProcess;
	private final RedisService redisService;

	public String uploadFile(MultipartFile file, User user) throws IOException {

		try {
			int nextVersionOrDefault = geNextVersionOrDefault(file.getOriginalFilename(), user.getId());
			VideoProcess videoSave = nextVersionOrDefault== 1?
					videoProcessRepository.save(new VideoProcess(user, file.getOriginalFilename(), nextVersionOrDefault)):
					updateVersionVideo(user.getId(),file.getOriginalFilename(), nextVersionOrDefault);
			String uploadResponse = uploadProcess.uploadFile(user.getId(), videoSave.getId(), nextVersionOrDefault, file);
			redisService.sendStatus(new InfoVideo(videoSave.getId(), user.getId(), nextVersionOrDefault, Stage.UPLOAD_DISK_DONE), StatusRequest.DISK_STATUS);
			return uploadResponse;
		}
		catch (Exception e ){
			throw new IOException("Erro ao realizar Upload: " + e.getMessage());
		}
	}

	public VideoProcess updateVersionVideo(Long userId, String name, Integer version){
		videoProcessRepository.updateVersion(userId, name, version);
		return videoProcessRepository.findByNameAndUserId(userId, name);
	}

	public int geNextVersionOrDefault(String videoName, Long userId) {
		Optional<Integer> lastVersion = videoProcessRepository.findLastVersion(videoName, userId);
		return lastVersion.map(v -> v + 1).orElse(1);
	}

}
