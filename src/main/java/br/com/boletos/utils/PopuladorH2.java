package br.com.boletos.utils;

import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.repositories.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.boletos.model.Boleto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Configuration
public class PopuladorH2 implements CommandLineRunner {

    @Autowired
    private BoletoRepository boletoRepository;

    @Override
    public void run(String... args) throws Exception {
        boletoRepository.save(new Boleto(null, "4fd251b7-0358-433f-b5b5-135b37538c1d", new BigDecimal("1000.00"), LocalDate.of(2023, 05, 06), "26133117036", "João", "João", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, "041dc250-724e-45bd-97c5-e3fd1021bebb", new BigDecimal("500.00"), LocalDate.of(2023, 07, 20), "45522000000190", "Padaria do pão", "Padaria do pão", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, "c880dbe7-bc59-460d-9965-c41534b18f3b", new BigDecimal("320.00"), LocalDate.of(2023, 07, 20), "94738515020", "Maria", "Maria", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, "313b11d6-61a3-46a3-8d47-6840bdc80132", new BigDecimal("200.00"), LocalDate.of(2023, 07, 20), "56207226000112", "Mercado123", "Mercado 123", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, "296fc370-f5be-4024-b017-9d2d89eb2639", new BigDecimal("200.00"), LocalDate.of(2023, 07, 03), "61428051015", "José", "José", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, "3dc5ce0b-bc05-4112-82a5-e8caa95243eb", new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "68218759000189", "Internet do Bairro", "Internet do bairro", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, "ad89ba0d-3193-4daa-875f-581657f3d875", new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "02733709011", "Marcos", "Marcos", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, "e7837eb9-07fe-46eb-b3e0-1dab027aab85", new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "00037470000120", "Banco 321", "Banco 321", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, "46be9eee-519e-4849-9c51-afce3cf642ad", new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "60099040050", "Adalberto", "Adalberto", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, "d60c2fe8-1272-4bae-9334-2012976ebc58", new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "62631979000153", "Academia aaa", "Academia aaa", SituacaoBoleto.EM_ABERTO));

    }

}

