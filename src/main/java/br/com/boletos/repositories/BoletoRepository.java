package br.com.boletos.repositories;

import br.com.boletos.model.SituacaoBoleto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.boletos.model.Boleto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    Optional<Boleto> findByUuid(String uuid);
    Optional<Boleto> findByUuidAndSituacao(String uuid, SituacaoBoleto situacaoBoleto);
    Optional<Boleto> findByIdAndDocumentoPagador(Integer idBoleto, String documentoPagador);
    
}
