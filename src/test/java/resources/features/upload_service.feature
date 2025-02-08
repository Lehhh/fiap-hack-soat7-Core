Feature: Upload de Arquivo com UploadService

  Scenario: Upload de um novo arquivo de vídeo
    Given um usuário com ID "123" e nome "João"
    And um arquivo de vídeo chamado "meu_video.mp4"
    When o usuário faz upload do arquivo
    Then o upload é realizado com sucesso
    And uma nova entrada de VideoProcess é criada no banco de dados

  Scenario: Upload de uma nova versão de um arquivo de vídeo existente
    Given um usuário com ID "123" e nome "João"
    And um arquivo de vídeo chamado "meu_video.mp4" já existe com a versão "1"
    When o usuário faz upload do arquivo
    Then o upload é realizado com sucesso
    And a versão do VideoProcess é atualizada no banco de dados para "2"

  Scenario: Falha no upload do arquivo
    Given um usuário com ID "123" e nome "João"
    And um arquivo de vídeo chamado "meu_video.mp4"
    When o upload falha devido a um erro de IO
    Then uma exceção de IO é lançada