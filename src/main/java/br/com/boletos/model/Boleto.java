package br.com.boletos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "boleto")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Boleto {
    
   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
	private Integer id;

    @Id
    @Column(name = "uuidAssociado", nullable = false, columnDefinition = "varchar(36)")
    private String UuidAssociado;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "vencimento", nullable = false)
    private LocalDate vencimento;

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
