package br.com.boletos.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoletoPagamentoDTO {

    private String id;
    private String documentoPagador;
    private String valor;

}
