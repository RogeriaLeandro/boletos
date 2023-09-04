package br.com.boletos.service;

import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.repositories.BoletoRepository;
import br.com.boletos.v1.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static br.com.boletos.BoletoTestHelper.*;
import static br.com.boletos.model.SituacaoBoleto.EM_ABERTO;
import static br.com.boletos.model.SituacaoBoleto.PAGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        var boleto = criarBoletos().get(0);
        boleto.setUuidAssociado(UUID.fromString(ID_ASSOCIADO));
        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        var actual = target.consultarBoletosPorUuid(ID_ASSOCIADO);
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociado() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        var actual = target.consultarBoletosPorUuid(ID_ASSOCIADO);
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuidAssociado(ID_ASSOCIADO);
        verifyNoInteractions(associadoService);
    }

    void consultarBoletosPorUuidAssociadoESituacaoBoleto() {
        var boleto = criarBoletos().get(0);
        boleto.setUuidAssociado(UUID.fromString(ID_ASSOCIADO));
        boleto.setSituacao(PAGO);
        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
        doReturn(Optional.of(boleto)).when(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        var actual = target.consultarBoletosPorUuidEPorSituacao(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        assertEquals(boletoDTO, actual);
        verify(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        verifyNoInteractions(associadoService);
    }

    @Test
    void nenhumBoletoEncontradoAoConsultarBoletosPorUuidAssociadoeSituacaoBoleto() {
        doReturn(Optional.empty()).when(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        var actual = target.consultarBoletosPorUuidEPorSituacao(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        assertEquals(Optional.empty(), actual);
        verify(boletoRepository).findByUuidAssociadoAndSituacaoBoleto(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        verifyNoInteractions(associadoService);
    }

    @Test
    void deveEfetuarPagamento() {
        var boleto = criarBoletos().get(0);
        boleto.setUuidAssociado(UUID.fromString(ID_ASSOCIADO));
        boleto.setSituacao(EM_ABERTO);
        boleto.setVencimento(LocalDate.now());
        var valor = new BigDecimal("500.00");
        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
        doReturn(Optional.of(boleto)).when(target).efetuaPagamento(CPF, 10000, new BigDecimal("500.00"));
        assertTrue(boleto.getSituacao().equals(PAGO));
        //verify(boletoRepository).save(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
        //verifyNoInteractions(associadoService);
    }

//    @Test
//    void deveEfetuarPagamento() {
//        var boleto = criarBoletos().get(0);
//        boleto.setUuidAssociado(UUID.fromString(ID_ASSOCIADO));
//        boleto.setSituacao(EM_ABERTO);
//        boleto.setVencimento(LocalDate.now());
//        var valor = new BigDecimal("500.00");
//        var boletoDTO = Optional.of(criarBoletoDTO(boleto));
//        doReturn(Optional.of(boleto)).when(target).efetuaPagamento(CPF, 10000, new BigDecimal("500.00"));
//        assertTrue(boleto.getSituacao().equals(PAGO));
//        //verify(boletoRepository).save(ID_ASSOCIADO, PAGO.getDescricaoSituacaoBoleto());
//        //verifyNoInteractions(associadoService);
//    }

}
