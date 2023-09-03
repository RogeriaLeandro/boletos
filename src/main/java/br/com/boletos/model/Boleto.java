package br.com.boletos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "boleto")
@Data
public class Boleto {
    
   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBoleto", updatable = false, unique = true, nullable = false)
	private Integer idBoleto;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "vencimento", nullable = false)
    private LocalDate vencimento;

    @Column(name = "uuidAssociado", nullable = false, columnDefinition = "varchar(36)")    
    private UUID UuidAssociado;

    @Column(name = "documentoPagador", nullable = false, length = 50)    
    private String documentoPagador;

    @Column(name = "nomePagador", nullable = false, length = 50)    
    private String nomePagador;

    @Column(name = "nomeFantasiaPagador", nullable = false, length = 50)    
    private String nomeFantasiaPagador;

    @Column(name = "situacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoBoleto situacao;

}
