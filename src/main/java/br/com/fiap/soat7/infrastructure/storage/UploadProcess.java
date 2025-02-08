package br.com.fiap.soat7.infrastructure.storage;

import br.com.fiap.soat7.infrastructure.configuration.TextReponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@Log4j2
public class UploadProcess {

	// Caminho do diretório onde os vídeos serão armazenados
	@Value("${br.com.fiap.soat7.upload.dir}")
	private String uploadDir;
	public String uploadFile(Long userId, Long videoId,Integer version, MultipartFile file) throws IOException {
		// Verificar se o arquivo está vazio
		String finalPath = String.format(uploadDir, userId, videoId, version);
		if (file.isEmpty()) {
			log.error(TextReponse.UPLOAD_DISK_ERROR_EMPTY);
			return TextReponse.UPLOAD_DISK_ERROR_EMPTY;
		}
		try {
			// Criar diretório se não existir
			File directory = new File(finalPath);
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					log.info("Diretório criado: " + directory.getAbsolutePath());
				} else {
					log.error("Falha ao criar o diretório: " + directory.getAbsolutePath());
					return TextReponse.UPLOAD_DISK_ERROR_FAIL_CREATE_DIRECTORY;
				}
			}
			// Salvar o arquivo no diretório configurado
			File uploadFile = new File(finalPath + file.getOriginalFilename());
			file.transferTo(uploadFile);
			log.info(String.format(TextReponse.SUCCESS_DISK_UPLOAD, file.getOriginalFilename()));
			return String.format(TextReponse.SUCCESS_DISK_UPLOAD,file.getOriginalFilename());
		} catch (IOException e) {
			log.error(TextReponse.UPLOAD_DISK_ERROR_FAIL_UPLOAD);
			e.printStackTrace();
			return String.format(TextReponse.UPLOAD_DISK_ERROR_FAIL_UPLOAD, e.getMessage());
		}
	}
}
