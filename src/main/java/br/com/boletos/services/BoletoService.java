package br.com.boletos.services;

import br.com.boletos.model.Boleto;
import br.com.boletos.model.BoletoDTO;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.repositories.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
}
