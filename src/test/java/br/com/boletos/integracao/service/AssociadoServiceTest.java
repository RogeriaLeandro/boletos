package br.com.boletos.integracao.service;

import br.com.boletos.integracao.associado.client.AssociadoClient;
import br.com.boletos.integracao.associado.service.AssociadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.boletos.BoletoTestHelper.ID_ASSOCIADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AssociadoServiceTest {

    @Mock
    private AssociadoClient associadoClient;

    @InjectMocks
    private AssociadoService target;

//    @Test
//    void associadoEncontrado() {
//        doReturn(true).when(associadoClient).consultarAssociado(ID_ASSOCIADO);
//        assertEquals(target.associadoECadastrado(ID_ASSOCIADO));
//        verify(associadoClient).consultarAssociado(ID_ASSOCIADO);
//    }

//    @Test
//    void semBoletosEmAbertoAoConsultarBoletosPorAssociado() {
//        doReturn(criarBoletosTodosPagos(ID_ASSOCIADO)).when(boletoClient).consultarBoletos(ID_ASSOCIADO);
//        assertFalse(target.possuiBoletoAPagar(ID_ASSOCIADO));
//        verify(boletoClient).consultarBoletos(ID_ASSOCIADO);
//    }
//
//    @Test
//    void boletoEmAbertoAoConsultarBoletoPorAssociado() {
//        doReturn(criarBoletosUmAPagar(ID_ASSOCIADO)).when(boletoClient).consultarBoletos(ID_ASSOCIADO);
//        assertTrue(target.possuiBoletoAPagar(ID_ASSOCIADO));
//        verify(boletoClient).consultarBoletos(ID_ASSOCIADO);
//    }

}

