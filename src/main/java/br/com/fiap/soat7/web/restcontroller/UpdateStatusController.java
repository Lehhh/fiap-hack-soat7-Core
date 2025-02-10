package br.com.fiap.soat7.web.restcontroller;

import br.com.fiap.soat7.application.service.UpdateStatusService;
import br.com.fiap.soat7.domain.dto.StatusRequestUpdate;
import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class UpdateStatusController {

	private final UpdateStatusService updateStatusService;

	@PutMapping("/s3/video/{videoId}")
	public ResponseEntity<Map<String, Boolean>> updateStatusUploadS3Video(@RequestParam Long videoId, @RequestBody StatusRequestUpdate statusRequestUpdate){
		Boolean isUpdated = updateStatusService.updateStatusS3Video(videoId, statusRequestUpdate.getStage());
		return isUpdated ? ResponseEntity.ok(Map.of(TextReponse.MESSAGE, true)) : ResponseEntity.ok(Map.of(TextReponse.MESSAGE, false));
	}
	@PutMapping("/process/video/{videoId}")
	public ResponseEntity<Map<String, Boolean>> updateStatusProcessVideo(@RequestParam Long videoId, @RequestBody StatusRequestUpdate statusRequestUpdate){
		Boolean isUpdated = updateStatusService.updateStatusProcessVideo(videoId, statusRequestUpdate.getStage());

		return isUpdated ? ResponseEntity.ok(Map.of(TextReponse.MESSAGE, true)) : ResponseEntity.ok(Map.of(TextReponse.MESSAGE, false));
	}
	@PutMapping("/s3/images/{videoId}")
	public ResponseEntity<Map<String, Boolean>> updateStatusUploadS3Images(@RequestParam Long imageId, @RequestBody StatusRequestUpdate statusRequestUpdate){
		Boolean isUpdated = updateStatusService.updateStatusS3Image(imageId, statusRequestUpdate.getStage());

		return isUpdated ? ResponseEntity.ok(Map.of(TextReponse.MESSAGE, true)) : ResponseEntity.ok(Map.of(TextReponse.MESSAGE, false));
	}
}
