package br.com.fiap.soat7.web.controller;

import br.com.fiap.soat7.application.service.UploadService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class VideoController {
	@GetMapping("/upload")
	public String showUploadForm(Model model) {
		return "upload";
	}

}