package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RoleNames {
	ADMIN, USER, MODERATOR;

	public static List<String> fetchRoles(){
		return Arrays.stream(RoleNames.values())
				.map(Enum::name)  // Pega o nome de cada valor
				.toList();
	}

}
