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
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("1000.00"), LocalDate.of(2023, 05, 06), "26133117036", "João", "João", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("500.00"), LocalDate.of(2023, 07, 20), "45522000000190", "Padaria do pão", "Padaria do pão", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("320.00"), LocalDate.of(2023, 07, 20), "94738515020", "Maria", "Maria", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 20), "56207226000112", "Mercado123", "Mercado 123", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 03), "61428051015", "José", "José", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "68218759000189", "Internet do Bairro", "Internet do bairro", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "02733709011", "Marcos", "Marcos", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "00037470000120", "Banco 321", "Banco 321", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "60099040050", "Adalberto", "Adalberto", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.randomUUID().toString(), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "62631979000153", "Academia aaa", "Academia aaa", SituacaoBoleto.EM_ABERTO));

    }

}

