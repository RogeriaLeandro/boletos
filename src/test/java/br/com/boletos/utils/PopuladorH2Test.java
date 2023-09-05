package br.com.boletos.utils;

import br.com.boletos.BoletoTestHelper;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.repositories.BoletoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PopuladorH2Test {

    @Mock
    private BoletoRepository boletoRepository;

    @Test
    void deveSalvarBoleto() {
        var boleto = BoletoTestHelper.criarBoletos().get(0);
        boleto.setSituacao(SituacaoBoleto.EM_ABERTO);
        doReturn(boleto).when(boletoRepository).save(boleto);
        assertEquals("10000", boleto.getId().toString());
        verify(boletoRepository).save(boleto);
    }

}
