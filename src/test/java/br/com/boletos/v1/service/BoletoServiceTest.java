package br.com.boletos.v1.service;


import br.com.boletos.exceptions.*;
import br.com.boletos.integracao.associado.service.AssociadoService;

import br.com.boletos.repositories.BoletoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
        boleto.setUuid(ID_ASSOCIADO);

        var boletoDTO = Optional.of(criarBoletoDTO(boleto));

        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuid(ID_ASSOCIADO);
        var actual = target.consultarBoletoPorUuid(ID_ASSOCIADO);
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuid(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociado() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuid(ID_ASSOCIADO);
        var actual = target.consultarBoletoPorUuid(ID_ASSOCIADO);
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuid(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void consultarBoletosPorUuidAssociadoESituacaoBoleto() {
        var boleto = criarBoletos().get(0);
        boleto.setUuid(ID_ASSOCIADO);
        boleto.setSituacao(PAGO);
        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuidAndSituacao(ID_ASSOCIADO, PAGO);
        var actual = target.consultarBoletoPorUuidEPorSituacao(ID_ASSOCIADO, PAGO);
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuidAndSituacao(ID_ASSOCIADO, PAGO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociadoeSituacaoBoleto() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuidAndSituacao(ID_ASSOCIADO, PAGO);
        var actual = target.consultarBoletoPorUuidEPorSituacao(ID_ASSOCIADO, PAGO);
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuidAndSituacao(ID_ASSOCIADO, PAGO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void deveEfetuarPagamento() {
        var boleto = criarBoletos().get(0);
        boleto.setSituacao(EM_ABERTO);
        boleto.setDocumentoPagador("07617436777");
        boleto.setId(6);
        boleto.setValor(new BigDecimal("200.00"));
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        doReturn(boleto).when(boletoRepository).save(boleto);
        doReturn(true).when(associadoService).associadoECadastrado(boleto.getUuid());
        target.efetuaPagamento(boleto.getDocumentoPagador(), boleto.getId().toString(), boleto.getValor());
        assertEquals(boleto.getSituacao(), PAGO);
        verify(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        verify(boletoRepository).save(boleto);
        verify(associadoService).associadoECadastrado(boleto.getUuid());

    }


    @Test
    void deveValidarAssociado() {
        var boleto = criarBoletos().get(0);
        boleto.setSituacao(EM_ABERTO);
        boleto.setUuid("4fd251b7-0358-433f-b5b5-135b37538c1d");
        boleto.setDocumentoPagador("076.174.367-77");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        doReturn(false).when(associadoService).associadoECadastrado(boleto.getUuid());
        var exception = assertThrows(AssociadoNaoExisteNaAPIException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                        boleto.getId().toString(),
                                                                                                        boleto.getValor()));
        assertEquals("Associado não cadastrado em Associados.", exception.getMessage());
        verify(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(boleto.getUuid());
        verifyNoMoreInteractions(boletoRepository);
    }

    @Test
    void deveValidarSeValorBoletoEDivergente() {
        var boleto = criarBoletos().get(0);
        var valor = new BigDecimal("500.00");
        boleto.setDocumentoPagador("076.174.367-77");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        var exception = assertThrows(ValorDeBoletoDivergenteNoPagamentoException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                    boleto.getId().toString(),
                                                                                                                    valor));
        assertEquals("Não é possível efetuar pagamento pois os valores são divergentes.", exception.getMessage());
        verify(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        verifyNoInteractions(associadoService);
        verifyNoMoreInteractions(boletoRepository);
    }

    @Test
    void deveValidarSeBoletoJaFoiPago() {
        var boleto  = criarBoletos().get(0);
        boleto.setSituacao(PAGO);
        boleto.setDocumentoPagador("076.174.367-77");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        var exception = assertThrows(BoletoPagoException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                boleto.getId().toString(),
                                                                                                                boleto.getValor()));

        assertEquals("Não é possível efetuar pagamento de um boleto já pago.", exception.getMessage());
        verify(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        verifyNoMoreInteractions(boletoRepository);
        verifyNoInteractions(associadoService);
    }

    @Test
    void deveValidarSeVencimentoBoletoAntesDataAtual() {
        var boleto = criarBoletos().get(0);
        boleto.setVencimento(LocalDate.of(2024,07,03));
        boleto.setSituacao(EM_ABERTO);
        boleto.setId(10000);
        boleto.setDocumentoPagador("076.174.367-77");
        doReturn(Optional.of(boleto)).when(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        doReturn(false).when(associadoService).associadoECadastrado(boleto.getUuid());
        var exception = assertThrows(AssociadoNaoExisteNaAPIException.class, () -> target.efetuaPagamento(boleto.getDocumentoPagador(),
                                                                                                                boleto.getId().toString(),
                                                                                                                boleto.getValor()));

        assertEquals("Associado não cadastrado em Associados.", exception.getMessage());
        verify(boletoRepository).findByIdAndDocumentoPagador(boleto.getId(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(boleto.getUuid());
        verifyNoMoreInteractions(boletoRepository);

    }
}
