package br.com.boletos.rabbitmq.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListenerMensagemPagamentoTest {

    @InjectMocks
    public ListenerMensagemPagamento listener;

    @Test
    void deveRetornarStringFormatadaBigDecimal() {
        var numero = "00000000000000020000";
        var numeroTratado = listener.trataValor(numero);
        assertEquals("200.00", numeroTratado);
    }

}