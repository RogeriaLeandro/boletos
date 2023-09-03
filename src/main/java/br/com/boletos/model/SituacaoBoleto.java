package br.com.boletos.model;

public enum SituacaoBoleto {
    
    EM_ABERTO("Em Aberto"),
    PAGO("Pago"),
    EM_ATRASO("Em Atraso");

    private String descricaoSituacaoBoleto;

    SituacaoBoleto(String descricaoSituacaoBoleto){
        this.descricaoSituacaoBoleto = descricaoSituacaoBoleto;
    }

    public String getDescricaoSituacaoBoleto() {
        return descricaoSituacaoBoleto;
    }

}
