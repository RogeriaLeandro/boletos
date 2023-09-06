package br.com.boletos.integracao.associado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociadoDTO {

    private String id;

    private String documento;

    private String tipoPessoa;

    private String nome;

}
