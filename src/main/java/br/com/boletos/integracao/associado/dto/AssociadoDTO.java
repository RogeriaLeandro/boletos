package br.com.boletos.integracao.associado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class AssociadoDTO {

    @Schema(description = "Identificador único do associado", example = "afffaf58-62a9-4808-98c7-e5d1a7d4b1c2")
    @NotBlank
    private String uuidAssociado;

    @Schema(description = "Documento do associado. CPF ou CNPJ", example = "123.456.789-43")
    @NotBlank
    private String documento;

    @Schema(description = "Tipo pessoa do associado.", example = "PF")
    private String tipoPessoa;

    @Schema(description = "Nome do associado.", example = "Adalberto Rodrigues")
    @NotBlank
    private String nome;

}
