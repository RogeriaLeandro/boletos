package br.com.boletos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boleto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Boleto {
    
   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBoleto", updatable = false, unique = true, nullable = false)
	private Long idBoleto;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "vencimento", nullable = false)
    private LocalDate vencimento;

    @Column(name = "uuidAssociado", nullable = false, columnDefinition = "varchar(36)")    
    private UUID UUIDAssociado;

    @Column(name = "documentoPagador", nullable = false, length = 50)    
    private String documentoPagador;

    @Column(name = "nomePagador", nullable = false, length = 50)    
    private String nomePagador;

    @Column(name = "nomeFantasiaPagador", nullable = false, length = 50)    
    private String nomeFantasiaPagador;

    @Column(name = "situacao", nullable = false)    
    private String situacao;


    @Override
    public String toString() {
        return "{" +
            " idBoleto='" + getIdBoleto() + "'" +
            ", valor='" + getValor() + "'" +
            ", vencimento='" + getVencimento() + "'" +
            ", UUIDAssociado='" + getUUIDAssociado() + "'" +
            ", documentoPagador='" + getDocumentoPagador() + "'" +
            ", nomePagador='" + getNomePagador() + "'" +
            ", nomeFantasiaPagador='" + getNomeFantasiaPagador() + "'" +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }


}
