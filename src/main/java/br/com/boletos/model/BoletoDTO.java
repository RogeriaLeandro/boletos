package br.com.boletos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class BoletoDTO {

    @Schema(description = "Identificador único do Boleto")
    @NotBlank
    private String idBoleto;

    @Schema(description = "Valor do Boleto", example = "150,25")
    @NotBlank
    private BigDecimal valor;

    @Schema(description = "Data de Vencimento")
    @NotBlank
    private LocalDate vencimento;

    @Schema(description = "UUID do Associado", example = "afffaf58-62a9-4808-98c7-e5d1a7d4b1c2")
    @NotBlank
    private String uuidAssociado;

    @Schema(description = "Nome do associado.", example = "Adalberto Rodrigues")
    @NotBlank
    private String nome;

    @Schema(description = "Documento do associado. CPF ou CNPJ", example = "123.456.789-43")
    @NotBlank
    private String documento;

    @Schema(description = "Nome Fantasia do associado", example = "Adalberto Rodrigues")
    @NotBlank
    private String nomeFantasia;

    @Schema(description = "Situação do Boleto", example = "PF")
    private SituacaoBoleto situacaoBoleto;

}
