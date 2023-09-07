package br.com.boletos.rabbitmq.consumer;

import br.com.boletos.exceptions.DocumentoInvalidoException;
import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.BoletoPagamentoDTO;
import br.com.boletos.repositories.BoletoRepository;
import br.com.boletos.v1.service.BoletoService;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class ListenerMensagemPagamento {

    @Autowired
    public BoletoService boletoService;

    @Autowired
    public AssociadoService associadoService;

    @Autowired
    public BoletoRepository boletoRepository;

    private static Logger logger = LoggerFactory.getLogger(ListenerMensagemPagamento.class);

    @RabbitListener(queues = "queue")
    public void listener(@Payload String payload, Channel channel,
                         @Header("amqp_deliveryTag") long tag) throws IOException {
        channel.basicAck(tag, false);

        var mapper = new ObjectMapper();
        try {
            var boletoPagamentoDTO = mapper.readValue(payload, BoletoPagamentoDTO.class);
            boletoService.efetuaPagamento(boletoPagamentoDTO.getDocumentoAssociado(), boletoPagamentoDTO.getIdBoleto(), new BigDecimal(boletoPagamentoDTO.getValorBoleto()));
        } catch (Exception e) {
            logger.error("Json não processado - " + e.getMessage());
        }

    }

    public String trataValor(String valor) {

        BigDecimal valorTratado = new BigDecimal(valor);
        String valorString = valorTratado.toString();
        StringBuffer stringBuffer = new StringBuffer(valorString);

        stringBuffer.insert(valorString.length() - 2, ".");
        return stringBuffer.toString();

    }

    public String trataIdBoleto(String id) {

        return id.trim();
    }

    public String trataDocumento(String documentoPagador) {
        String cpf = documentoPagador.substring(3, 11);
        if(this.documentoEValido(cpf)){
            return cpf;
        }

        if(this.documentoEValido(documentoPagador)){
            return documentoPagador;
        }
        throw new DocumentoInvalidoException("Documento inválido");
    }


    public boolean documentoEValido(String documento) {

        documento = documento.replace("-", "");
        documento = documento.replace(".", "");

        if (documento.length() == 11) {
            CPFValidator cpfValidator = new CPFValidator();
            try {
                cpfValidator.assertValid(documento);
                return true;
            } catch (Exception e) {
                logger.error("CPF Inválido");
            }
        } else {
            CNPJValidator cnpjValidator = new CNPJValidator();
            try {
                cnpjValidator.assertValid(documento);
                return true;
            } catch (Exception e) {
                logger.error("CNPJ Inválido");
            }
        }

        return false;
    }

}
