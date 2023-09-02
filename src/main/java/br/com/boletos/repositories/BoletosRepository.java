package br.com.boletos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.boletos.model.Boletos;

public interface BoletosRepository extends JpaRepository<Boletos, Long> {
    
}
