package br.com.boletos;

import br.com.boletos.model.Boleto;
import br.com.boletos.v1.dto.BoletoDTO;
import br.com.boletos.model.SituacaoBoleto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.boletos.model.SituacaoBoleto.PAGO;
import static br.com.boletos.model.SituacaoBoleto.EM_ABERTO;

public class BoletoTestHelper {

    public static final String CPF = "123.123.123-43";
    public static final String CNPJ = "91.957.952/0001-53";
    public static final String CPF_NAO_FORMATADO = "12312312343";
    public static final String CNPJ_NAO_FORMATADO = "91957952000153";
    public static final String ID_ASSOCIADO = "32ce9e27-926a-458a-9a1e-c822d164c167";

    public static List<BoletoDTO> criarBoletosDTO() {
        return List.of(
                criarBoletoDTO(PAGO), criarBoletoDTO(EM_ABERTO));
    }

    public static List<Boleto> criarBoletos() {
        return List.of(
                criarBoleto(PAGO), criarBoleto(EM_ABERTO));
    }

    private static Boleto criarBoleto(SituacaoBoleto situacaoBoleto) {
        var boleto = new Boleto();
        boleto.setIdBoleto(10000);
        boleto.setValor(new BigDecimal(500.00));
        boleto.setVencimento(LocalDate.now());
        boleto.setUuidAssociado(UUID.fromString(ID_ASSOCIADO));
        boleto.setDocumentoPagador(CPF);
        boleto.setNomePagador("Nome");
        boleto.setNomeFantasiaPagador("NomeFantasia");
        return boleto;
    }

    public static BoletoDTO criarBoletoDTO(SituacaoBoleto situacaoBoleto) {
        return BoletoDTO.builder()
                .idBoleto("100001")
                .valor(new BigDecimal(10000.00))
                .vencimento(LocalDate.now())
                .uuidAssociado(ID_ASSOCIADO)
                .nome("João")
                .documento(CPF)
                .nomeFantasia("João Marcelo")
                .situacaoBoleto(situacaoBoleto)
                .build();
    }

    public static BoletoDTO criarBoletoDTO(Boleto boleto) {
        return BoletoDTO.builder()
                .idBoleto(boleto.getIdBoleto().toString())
                .valor(boleto.getValor())
                .vencimento(boleto.getVencimento())
                .uuidAssociado(boleto.getUuidAssociado().toString())
                .nome(boleto.getNomePagador())
                .documento(boleto.getDocumentoPagador())
                .nomeFantasia(boleto.getNomeFantasiaPagador())
                .situacaoBoleto(boleto.getSituacao())
                .build();
    }

    public static List<BoletoDTO> criarBoletosDTO(List<Boleto> boletos) {
        return boletos.stream().map(boleto -> criarBoletoDTO(boleto)).collect(Collectors.toList());
    }

}
