package br.com.boletos.rabbitmq.consumer;

import br.com.boletos.integracao.associado.service.AssociadoService;
import br.com.boletos.model.BoletoPagamentoDTO;
import br.com.boletos.repositories.BoletoRepository;
import br.com.boletos.v1.service.BoletoService;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void listener(String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        try {

            BoletoPagamentoDTO boletoPagamentoDTO = mapper.readValue(json, BoletoPagamentoDTO.class);

            String documento = trataDocumento(boletoPagamentoDTO.getDocumentoPagador());
            String idBoleto = trataIdBoleto(boletoPagamentoDTO.getId());
            String valor = trataValor(boletoPagamentoDTO.getValor());

            boletoService.efetuaPagamento(documento, idBoleto, new BigDecimal(valor));

        } catch (JsonProcessingException e) {
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
        boolean cpfEValido = this.documentoEValido(cpf);
        boolean cnpjEValido = this.documentoEValido(documentoPagador);

        try {
            if (cpfEValido) {
                return cpf;
            }

            if (cnpjEValido) {
                return documentoPagador;
            }
        } catch (Exception e) {
            return documentoPagador;
        }

        return documentoPagador;
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
                return false;
            }
        } else {
            CNPJValidator cnpjValidator = new CNPJValidator();
            try {
                cnpjValidator.assertValid(documento);
                return true;
            } catch (Exception e) {
                logger.error("CNPJ Inválido");
                return false;
            }
        }
    }

}
