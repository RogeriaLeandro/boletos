package br.com.boletos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.boletos.model.Boleto;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    List<Boleto> findByUuidAssociado(String uuid);
    List<Boleto> findByUuidAssociadoAndSituacaoBoleto(String uuid, String situacaoBoleto);
    Boleto findByIdBoletoAndDocumentoPagador(Integer idBoleto, String documentoPagador);
    
}
