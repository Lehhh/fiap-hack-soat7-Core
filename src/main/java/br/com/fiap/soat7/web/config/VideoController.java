package br.com.fiap.soat7.web.config;

import br.com.fiap.soat7.application.service.UploadService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import br.com.fiap.soat7.infrastructure.storage.UploadProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VideoController {

	private final UploadService uploadService;
	private final UserRepository userRepository;

	@GetMapping("/upload")
	public String showUploadForm(Model model) {
		return "upload";
	}

	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
		User user = userRepository.findByEmail(userDetails.getUsername()).get();
		String uploadResponse = uploadService.uploadFile(file, user);
		return uploadResponse.contains(TextReponse.SUCCESS) ?
				ResponseEntity.ok().body(Map.of(TextReponse.MESSAGE, uploadResponse)) :
				ResponseEntity.internalServerError().body(Map.of(TextReponse.MESSAGE, uploadResponse));
	}
}