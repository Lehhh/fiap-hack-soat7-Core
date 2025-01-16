package br.com.fiap.soat7.domain.dto;

import br.com.fiap.soat7.domain.enums.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusRequestUpdate {
	private Stage stage;
}
