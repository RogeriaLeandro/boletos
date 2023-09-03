package br.com.boletos.v1.service;

import br.com.boletos.exceptions.BoletoPagoException;
import br.com.boletos.exceptions.ValorDeBoletoDivergenteNoPagamentoException;
import br.com.boletos.model.Boleto;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.v1.dto.BoletoDTO;
import br.com.boletos.repositories.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class BoletoService {

    @Autowired
    private BoletoRepository boletoRepository;

    @Value("${app.config.qtd-registros-pagina}")
    private int qtdRegistrosPorPagina = 5;

    public List<BoletoDTO> consultarBoletosPorUuid(String uuid) {
        return boletoRepository.findByUuidAssociado(uuid)
                .stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<BoletoDTO> consultarBoletosPorUuidEPorSituacao(String uuid, String situacaoBoleto) {
        return boletoRepository.findByUuidAssociadoAndSituacaoBoleto(uuid, situacaoBoleto)
                .stream()
                .map(this::toDTO).collect(Collectors.toList());
    }
    private BoletoDTO toDTO(Boleto boleto) {
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

    public void efetuaPagamento(String documentoAssociado, Integer idBoleto, BigDecimal valor) {
        Boleto boleto = boletoRepository.findByIdBoletoAndDocumentoPagador(idBoleto, documentoAssociado);

        if (valor != boleto.getValor()) {
            throw new ValorDeBoletoDivergenteNoPagamentoException("Não é possível efetuar pagamento pois os valores são divergentes.");
        }

        if (boleto.getSituacao().equals(SituacaoBoleto.PAGO)) {
            throw new BoletoPagoException("Não é possível efetuar pagamento de um boleto já pago.");
        }
        boleto.setSituacao(SituacaoBoleto.PAGO);
    }
}