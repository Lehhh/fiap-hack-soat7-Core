package br.com.fiap.soat7.infrastructure.configuration;

import lombok.Getter;

@Getter
public class TextReponse {

	public final static String SUCCESS = "Success: ";
	public final static String ERROR = "Erro : ";
	public final static String MESSAGE = "Message";
	public final static String ID = "id";

	public final static String USER_ID_NOT_FOUND = ERROR + "Usuário com ID %s nao foi encontrado";
	public final static String USER_NOT_FOUND = ERROR + "Usuário não encontrado";
	public final static String DIRECTORY_CREATED = SUCCESS + "Diretório criado: ";
	public final static String DIRECTORY_ERROR_CREATE = ERROR + "Falha ao criar o diretório: ";
	public final static String UPLOAD_DISK_ERROR_EMPTY = ERROR + "Arquivo Vazio";
	public final static String UPLOAD_DISK_ERROR_FAIL_UPLOAD = ERROR + "Upload não realizado %s";
	public final static String SUCCESS_DISK_UPLOAD = SUCCESS + "Upload de arquivo concluído com sucesso: %s";
	public final static String UPLOAD_DISK_ERROR_FAIL_CREATE_DIRECTORY = ERROR + "Ao criar diretorio";

}
