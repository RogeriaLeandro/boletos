package br.com.boletos.rabbitmq.consumer;

import br.com.boletos.model.BoletoPagamentoDTO;
import br.com.boletos.repositories.BoletoRepository;
import br.com.boletos.v1.service.BoletoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.Gson.GsonBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ListenerMensagemPagamento {

    @Autowired
    public BoletoService boletoService;

    @Autowired
    public BoletoRepository boletoRepository;

    @RabbitListener(queues = "queue")
    public void listener(String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        BoletoPagamentoDTO boletoPagamentoDTO = mapper.readValue(json, BoletoPagamentoDTO.class);

        String documento = trataDocumento(boletoPagamentoDTO.getDocumentoPagador());
        String idBoleto = trataIdBoleto(boletoPagamentoDTO.getId());
        String valor = trataValor(boletoPagamentoDTO.getValor());

        boletoService.efetuaPagamento(documento, idBoleto, new BigDecimal(valor));


    }

    private String trataValor(String valor) {
        return null;
    }

    private String trataIdBoleto(String id) {
        return null;
    }

    private String trataDocumento(String documentoPagador) {
        return null;
    }
}

//        Integer idBoleto = Integer.parseInt(boletoPagamentoDTO.getId().trim());
//        boletoRepository.findById(idBoleto);