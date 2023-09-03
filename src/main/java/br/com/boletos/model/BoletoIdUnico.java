package br.com.boletos.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class BoletoIdUnico {

    @EmbeddedId
    private Boleto id;
}
