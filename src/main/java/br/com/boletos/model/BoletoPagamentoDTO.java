package br.com.boletos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletoPagamentoDTO {

    @JsonProperty("documentoAssociado")
    private String documentoAssociado;

    @JsonProperty("idBoleto")
    private String idBoleto;

    @JsonProperty("valorBoleto")
    private String valorBoleto;

}
