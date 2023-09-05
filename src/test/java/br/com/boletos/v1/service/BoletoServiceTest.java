package br.com.boletos.v1.service;

import br.com.boletos.BoletoTestHelper;
import br.com.boletos.exceptions.AssociadoNaoExisteNaAPIException;
import br.com.boletos.exceptions.BoletoPagoException;
import br.com.boletos.exceptions.DataVencimentoAntesDataAtualException;
import br.com.boletos.exceptions.ValorDeBoletoDivergenteNoPagamentoException;
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

//    @Test
//    void deveEfetuarPagamento() {
//        var boleto = criarBoletos().get(0);
//        boleto.setDocumentoPagador(BoletoTestHelper.CPF);
//        boleto.setSituacao(EM_ABERTO);
//        boleto.setVencimento(LocalDate.now());
//        boleto.setId(1000);
//        boleto.setValor(new BigDecimal("500.00"));
//        doReturn(boleto).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
//        target.efetuaPagamento(boleto.getDocumentoPagador(), boleto.getId().toString(), boleto.getValor());
//        assertEquals(boleto.getSituacao(), PAGO);
//        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
//        verifyNoInteractions(associadoService);
//    }


    @Test
    void deveValidarAssociado() {
        var boleto = criarBoletos().get(0);
        boleto.setDocumentoPagador(CPF);
        boleto.setUuidAssociado(ID_ASSOCIADO);
        boleto.setId(1);
        doReturn(boleto).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(true).when(associadoService).associadoECadastrado(ID_ASSOCIADO);
        var exception = assertThrows(AssociadoNaoExisteNaAPIException.class, () -> target.validaAssociado(ID_ASSOCIADO));
        assertEquals("Associado não cadastrado em Associados.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(ID_ASSOCIADO);
    }

    @Test
    void deveValidarSeValorBoletoEDivergente() {
        var boleto = criarBoletos().get(0);
        boleto.setDocumentoPagador(CPF);
        boleto.setUuidAssociado(ID_ASSOCIADO);
        boleto.setId(1);
        var valor = boleto.getValor();
        doReturn(boleto).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(true).when(associadoService).associadoECadastrado(ID_ASSOCIADO);
        var exception = assertThrows(ValorDeBoletoDivergenteNoPagamentoException.class, () -> target.validaBoleto(boleto, valor));
        assertEquals("Não é possível efetuar pagamento pois os valores são divergentes.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(ID_ASSOCIADO);
    }

    @Test
    void deveValidarSeBoletoJaFoiPago() {
        var boleto = criarBoletos().get(0);
        boleto.setDocumentoPagador(CPF);
        boleto.setUuidAssociado(ID_ASSOCIADO);
        boleto.setId(1);
        var valor = boleto.getValor();
        doReturn(boleto).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(true).when(associadoService).associadoECadastrado(ID_ASSOCIADO);
        var exception = assertThrows(BoletoPagoException.class, () -> target.validaBoleto(boleto, valor));
        assertEquals("Não é possível efetuar pagamento de um boleto já pago.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(ID_ASSOCIADO);
    }

    @Test
    void deveValidarSeVencimentoBoletoAntesDataAtual() {
        var boleto = criarBoletos().get(0);
        boleto.setVencimento(LocalDate.of(2023,07,03));
        boleto.setSituacao(EM_ABERTO);
        doReturn(boleto).when(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        doReturn(true).when(associadoService).associadoECadastrado(ID_ASSOCIADO);
        var exception = assertThrows(DataVencimentoAntesDataAtualException.class, () -> target.validaBoleto(boleto, boleto.getValor()));
        assertEquals("Não é possível efetuar pagamento pois a Data de Vencimento Expirou.", exception.getMessage());
        verify(boletoRepository).findByIdBoletoAndDocumentoPagador(boleto.getId().toString(), boleto.getDocumentoPagador());
        verify(associadoService).associadoECadastrado(ID_ASSOCIADO);
    }


}
