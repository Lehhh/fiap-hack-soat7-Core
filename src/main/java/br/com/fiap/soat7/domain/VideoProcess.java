package br.com.fiap.soat7.domain;

import br.com.fiap.soat7.domain.enums.Stage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "video")
public class VideoProcess implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id") // Nome da chave estrangeira
	private User userId;
	private String name;
	private Integer version;
	private Stage stageUploadS3;
	private Stage stageProcessVideo;
	@OneToOne
	private ImageProcess imageProcess;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalDateTime endProcess;

	public VideoProcess(User userId, String name, Integer version) {
		this.userId = userId;
		this.name = name;
		this.version = version;
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}
}
