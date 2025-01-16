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
@Table(name = "image")
public class ImageProcess implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private VideoProcess videoProcess;
	private Stage stage;
	private LocalDateTime createdDate;

}