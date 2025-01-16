package br.com.fiap.soat7.domain.dto;

import br.com.fiap.soat7.domain.enums.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InfoVideo {
	private Long videoId;
	private Long userId;
	private Long imageId =0L;
	private Integer version;
	private Stage stage;

	public InfoVideo(Long videoId, Long userId, Integer version, Stage stage) {
		this.videoId = videoId;
		this.userId = userId;
		this.version = version;
		this.stage = stage;
	}
}
