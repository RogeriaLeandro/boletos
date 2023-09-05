package br.com.boletos.v1.service;

import br.com.boletos.BoletoTestHelper;
import br.com.boletos.exceptions.AssociadoNaoExisteNaAPIException;
import br.com.boletos.exceptions.BoletoPagoException;
import br.com.boletos.exceptions.DataVencimentoAntesDataAtualException;
import br.com.boletos.exceptions.ValorDeBoletoDivergenteNoPagamentoException;
import br.com.boletos.integracao.associado.client.AssociadoClient;
import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.repositories.BoletoRepository;
import br.com.boletos.v1.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static br.com.boletos.BoletoTestHelper.*;
import static br.com.boletos.model.SituacaoBoleto.EM_ABERTO;
import static br.com.boletos.model.SituacaoBoleto.PAGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoletoServiceTest {

    @Mock
    private BoletoRepository boletoRepository;

    @Mock
    private AssociadoService associadoService;

    @InjectMocks
    private BoletoService target;

    @Test
    void consultarBoletosPorUuidAssociado() {
        var boleto = criarBoletos().get(1);
        boleto.setUuidAssociado(ID_ASSOCIADO);

        var boletoDTO = Optional.of(criarBoletoDTO(boleto));

        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        var actual = target.consultarBoletoPorUuid(ID_ASSOCIADO);
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociado() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        var actual = target.consultarBoletoPorUuid(ID_ASSOCIADO);
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void consultarBoletosPorUuidAssociadoESituacaoBoleto() {
        var boleto = criarBoletos().get(0);
        boleto.setUuidAssociado(ID_ASSOCIADO);
        boleto.setSituacao(PAGO);
        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        var actual = target.consultarBoletoPorUuidEPorSituacao(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociadoeSituacaoBoleto() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        var actual = target.consultarBoletoPorUuidEPorSituacao(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        verifyNoInteractions(associadoService);
    }

    @Test
    void deveEfetuarPagamento() {
        var boleto = criarBoletos().get(0);
        boleto.setSituacao(EM_ABERTO);
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(boleto).when(boletoRepository).save(boleto);
        doReturn(true).when(associadoService).associadoECadastrado(boleto.getUuidAssociado());
        target.efetuaPagamento(boleto.getDocumentoPagador(), boleto.getId().toString(), boleto.getValor());
        assertEquals(boleto.getSituacao(), PAGO);
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(boletoRepository).save(boleto);
        verify(associadoService).associadoECadastrado(boleto.getUuidAssociado());

    }


    @Test
    void deveValidarAssociado() {
        var boleto = criarBoletos().get(0);
        boleto.setSituacao(EM_ABERTO);
        boleto.setUuidAssociado("111");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(false).when(associadoService).associadoECadastrado(boleto.getUuidAssociado());
        var exception = assertThrows(AssociadoNaoExisteNaAPIException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                        boleto.getId().toString(),
                                                                                                        boleto.getValor()));
        assertEquals("Associado não cadastrado em Associados.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(boleto.getUuidAssociado());
        verifyNoMoreInteractions(boletoRepository);
    }

    @Test
    void deveValidarSeValorBoletoEDivergente() {
        var boleto = criarBoletos().get(0);
        var valor = new BigDecimal("1.00");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        var exception = assertThrows(ValorDeBoletoDivergenteNoPagamentoException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                    boleto.getId().toString(),
                                                                                                                    valor));
        assertEquals("Não é possível efetuar pagamento pois os valores são divergentes.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verifyNoInteractions(associadoService);
        verifyNoMoreInteractions(boletoRepository);
    }

    @Test
    void deveValidarSeBoletoJaFoiPago() {
        var boleto  = criarBoletos().get(0);
        boleto.setSituacao(PAGO);
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        var exception = assertThrows(BoletoPagoException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                boleto.getId().toString(),
                                                                                                                boleto.getValor()));

        assertEquals("Não é possível efetuar pagamento de um boleto já pago.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verifyNoMoreInteractions(boletoRepository);
        verifyNoInteractions(associadoService);
    }

    @Test
    void deveValidarSeVencimentoBoletoAntesDataAtual() {
        var boleto = criarBoletos().get(0);
        boleto.setVencimento(LocalDate.of(2023,07,03));
        boleto.setSituacao(EM_ABERTO);
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        var exception = assertThrows(DataVencimentoAntesDataAtualException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                boleto.getId().toString(),
                                                                                                                boleto.getValor()));

        assertEquals("Não é possível efetuar pagamento pois a Data de Vencimento Expirou.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verifyNoMoreInteractions(boletoRepository);
        verifyNoInteractions(associadoService);
    }
}
