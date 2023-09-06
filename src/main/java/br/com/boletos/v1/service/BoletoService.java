package br.com.boletos.v1.service;

import br.com.boletos.exceptions.BoletoPagoException;
import br.com.boletos.exceptions.DataVencimentoAntesDataAtualException;
import br.com.boletos.exceptions.ValorDeBoletoDivergenteNoPagamentoException;
import br.com.boletos.exceptions.AssociadoNaoExisteNaAPIException;
import br.com.boletos.integracao.associado.client.AssociadoClient;
import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.Boleto;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.v1.dto.BoletoDTO;
import br.com.boletos.repositories.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoletoService {

    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private AssociadoService associadoService;

    @Value("${app.config.qtd-registros-pagina}")
    private int qtdRegistrosPorPagina = 5;

    public Optional<BoletoDTO> consultarBoletoPorUuid(String uuid) {
        return boletoRepository.findByUuid(uuid).map(this::toDTO);

    }

    public Optional<BoletoDTO> consultarBoletoPorUuidEPorSituacao(String uuid, String situacaoBoleto) {
        return boletoRepository.findByUuidAndSituacao(uuid, situacaoBoleto).map(this::toDTO);
    }
    private BoletoDTO toDTO(Boleto boleto) {
        return BoletoDTO.builder()
                .idBoleto(boleto.getId().toString())
                .valor(boleto.getValor())
                .vencimento(boleto.getVencimento())
                .uuidAssociado(boleto.getUuid().toString())
                .nome(boleto.getNomePagador())
                .documento(boleto.getDocumentoPagador())
                .nomeFantasia(boleto.getNomeFantasiaPagador())
                .situacaoBoleto(boleto.getSituacao())
                .build();
    }

    public void efetuaPagamento(String documentoAssociado, String idBoleto, BigDecimal valor) {
        Optional<Boleto> boleto = boletoRepository.findByIdAndDocumentoPagador(idBoleto, documentoAssociado);

        this.validaBoleto(boleto, valor);
        this.validaAssociado(boleto.get().getUuid().toString());

        boleto.get().setSituacao(SituacaoBoleto.PAGO);
        boletoRepository.save(boleto.get());
    }

    private void validaAssociado(String uuid) {

        if(!associadoService.associadoECadastrado(uuid)){
            throw new AssociadoNaoExisteNaAPIException("Associado não cadastrado em Associados.");
        }

    }

    private void validaBoleto(Optional<Boleto> boleto, BigDecimal valor) {

        if (valor != boleto.get().getValor()) {
            throw new ValorDeBoletoDivergenteNoPagamentoException("Não é possível efetuar pagamento pois os valores são divergentes.");
        }

        if (boleto.get().getSituacao().equals(SituacaoBoleto.PAGO)) {
            throw new BoletoPagoException("Não é possível efetuar pagamento de um boleto já pago.");
        }

        if (boleto.get().getVencimento().isBefore(LocalDate.now())) {
            throw new DataVencimentoAntesDataAtualException("Não é possível efetuar pagamento pois a Data de Vencimento Expirou.");
        }
    }
}
