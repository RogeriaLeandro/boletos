package br.com.boletos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.boletos.model.Boleto;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    
}
