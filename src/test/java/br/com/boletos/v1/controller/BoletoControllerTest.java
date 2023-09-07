package br.com.boletos.v1.controller;

import br.com.boletos.BoletoTestHelper;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.v1.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoletoControllerTest {

    @Mock
    private BoletoService boletoService;

    @InjectMocks
    private BoletoController target;

    @Test
    void consultarBoletoPorDocumento(){
        var boleto = BoletoTestHelper.criarBoletoDTO(SituacaoBoleto.EM_ABERTO);
        boleto.setUuidAssociado(BoletoTestHelper.ID_ASSOCIADO);
        var optionalBoleto = Optional.of(boleto);
        doReturn(optionalBoleto).when(boletoService).consultarBoletoPorUuid(BoletoTestHelper.ID_ASSOCIADO);
        var response = target.consultaBoletoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(boleto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuid(BoletoTestHelper.ID_ASSOCIADO);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarPorAssociado(){
        doReturn(Optional.empty()).when(boletoService).consultarBoletoPorUuid(BoletoTestHelper.ID_ASSOCIADO);
        var response = target.consultaBoletoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(null, response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuid(BoletoTestHelper.ID_ASSOCIADO);
    }

    @Test
    void consultarBoletoPagoPorDocumento(){
        var boleto = BoletoTestHelper.criarBoletoDTO(SituacaoBoleto.PAGO);
        boleto.setUuidAssociado(BoletoTestHelper.ID_ASSOCIADO);
        var optionalBoleto = Optional.of(boleto);
        doReturn(optionalBoleto).when(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.PAGO);
        var response = target.consultaBoletoPagoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(boleto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.PAGO);
    }

    @Test
    void nenhumBoletoPagoEncontradoAoConsultarPorAssociado(){
        doReturn(Optional.empty()).when(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.PAGO);
        var response = target.consultaBoletoPagoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(null, response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.PAGO);
    }

    @Test
    void consultarBoletoEmAbertoPorDocumento(){
        var boleto = BoletoTestHelper.criarBoletoDTO(SituacaoBoleto.EM_ABERTO);
        boleto.setUuidAssociado(BoletoTestHelper.ID_ASSOCIADO);
        var optionalBoleto = Optional.of(boleto);
        doReturn(optionalBoleto).when(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.EM_ABERTO);
        var response = target.consultaBoletoEmAbertoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(boleto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.EM_ABERTO);
    }

    @Test
    void nenhumBoletoEmAbertoEncontradoAoConsultarPorAssociado(){
        doReturn(Optional.empty()).when(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.EM_ABERTO);
        var response = target.consultaBoletoEmAbertoPorUUIDAssociado(BoletoTestHelper.ID_ASSOCIADO);
        assertEquals(null, response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(boletoService).consultarBoletoPorUuidEPorSituacao(BoletoTestHelper.ID_ASSOCIADO, SituacaoBoleto.EM_ABERTO);
    }

    @Test
    void deveEfetuarPagamento() {
        var boleto = BoletoTestHelper.criarBoletoDTO(SituacaoBoleto.EM_ABERTO);
        var response = target.efetuaPagamentoBoleto(boleto.getDocumento(), boleto.getIdBoleto().toString(), boleto.getValor());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(boletoService).efetuaPagamento(boleto.getDocumento(), boleto.getIdBoleto().toString(), boleto.getValor());
    }

}
