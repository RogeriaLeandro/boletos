package br.com.boletos.v1.service;

import br.com.boletos.exceptions.*;
import br.com.boletos.integracao.associado.client.AssociadoClient;
import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.Boleto;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.rabbitmq.consumer.ListenerMensagemPagamento;
import br.com.boletos.v1.dto.BoletoDTO;
import br.com.boletos.repositories.BoletoRepository;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(ListenerMensagemPagamento.class);

    public Optional<BoletoDTO> consultarBoletoPorUuid(String uuid) {
        return boletoRepository.findByUuid(uuid).map(this::toDTO);

    }

    public Optional<BoletoDTO> consultarBoletoPorUuidEPorSituacao(String uuid, SituacaoBoleto situacaoBoleto) {
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
        validarDocumento(documentoAssociado);
        var boleto = boletoRepository.findByIdAndDocumentoPagador(Integer.valueOf(idBoleto), documentoAssociado)
                .orElseThrow(() -> new BoletoNaoEncontradoException("Boleto não encontrado para o associado"));

        this.validaBoleto(boleto, valor);
        this.validaAssociado(boleto.getUuid().toString());

        boleto.setSituacao(SituacaoBoleto.PAGO);
        boletoRepository.save(boleto);
    }
    private void validaAssociado(String uuid) {

        if(!associadoService.associadoECadastrado(uuid)){
            throw new AssociadoNaoExisteNaAPIException("Associado não cadastrado em Associados.");
        }

    }

    private void validarDocumento(String documento) {
        documento = documento.replace("-", "");
        documento = documento.replace(".", "");
        if (documento.length() == 11) {
            var cpfValidator = new CPFValidator();
            try {
                cpfValidator.assertValid(documento);
                return;
            } catch (Exception e) {
                logger.warn("CPF Inválido");
            }
        } else {
            var cnpjValidator = new CNPJValidator();
            try {
                cnpjValidator.assertValid(documento);
                return;
            } catch (Exception e) {
                logger.warn("CNPJ Inválido");
            }
        }

        throw new DocumentoInvalidoException("Documento inválido");
    }

    private void validaBoleto(Boleto boleto, BigDecimal valor) {
        if (!valor.equals(boleto.getValor())) {
            throw new ValorDeBoletoDivergenteNoPagamentoException("Não é possível efetuar pagamento pois os valores são divergentes.");
        }

        if (boleto.getSituacao().equals(SituacaoBoleto.PAGO)) {
            throw new BoletoPagoException("Não é possível efetuar pagamento de um boleto já pago.");
        }

        if (boleto.getVencimento().isBefore(LocalDate.now())) {
            throw new DataVencimentoAntesDataAtualException("Não é possível efetuar pagamento pois a Data de Vencimento Expirou.");
        }
    }
}
