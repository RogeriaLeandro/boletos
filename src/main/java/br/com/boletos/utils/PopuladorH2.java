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
        boletoRepository.save(new Boleto(null, UUID.fromString("5c7f6cda-6d20-4f9d-b00c-01ccb3d17a45"), new BigDecimal("1000.00"), LocalDate.of(2023, 05, 06), "26133117036", "João", "João", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.fromString("2dd491df-997c-4111-b806-fe5553b4a892"), new BigDecimal("500.00"), LocalDate.of(2023, 07, 20), "45522000000190", "Padaria do pão", "Padaria do pão", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, UUID.fromString("32ce9e27-926a-458a-9a1e-c822d164c167"), new BigDecimal("320.00"), LocalDate.of(2023, 07, 20), "94738515020", "Maria", "Maria", SituacaoBoleto.PAGO));
        boletoRepository.save(new Boleto(null, UUID.fromString("492886f3-fbbd-4a25-9ebe-6308b8e072bd"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 20), "56207226000112", "Mercado123", "Mercado 123", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.fromString("16544f34-cd4e-412f-ba17-14d05cbe219f"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 03), "61428051015", "José", "José", SituacaoBoleto.EM_ATRASO));
        boletoRepository.save(new Boleto(null, UUID.fromString("8e09e97d-4279-48ab-9bf2-9d503fa6976a"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "68218759000189", "Internet do Bairro", "Internet do bairro", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.fromString("89f51b89-5971-4672-a6c7-529b993eccd7"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "02733709011", "Marcos", "Marcos", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.fromString("a5d52e43-dd0b-4aea-bbc5-97ddd077f9ee"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "00037470000120", "Banco 321", "Banco 321", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.fromString("e0a20b84-3a4e-48c5-bdb0-42f05a66ce20"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "60099040050", "Adalberto", "Adalberto", SituacaoBoleto.EM_ABERTO));
        boletoRepository.save(new Boleto(null, UUID.fromString("0b9452c6-1fa8-4ce5-851f-fc656ef9d2f6"), new BigDecimal("200.00"), LocalDate.of(2023, 07, 10), "62631979000153", "Academia aaa", "Academia aaa", SituacaoBoleto.EM_ABERTO));

    }

}

